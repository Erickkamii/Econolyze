import { TokenStore } from "@/lib/utils/token-store";

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/+$/, "") ?? "";
const AUTH_PATH = "/auth";

type ApiRequestInit = RequestInit & {
    _retry?: boolean;
    skipAuthRefresh?: boolean;
};

type ApiError = Error & {
    status?: number;
    details?: unknown;
};

let refreshPromise: Promise<string | null> | null = null;

function buildUrl(path: string): string {
    if (/^https?:\/\//.test(path)) {
        return path;
    }

    return `${API_BASE}${path}`;
}

async function parseError(response: Response, fallbackMessage: string): Promise<ApiError> {
    const text = await response.text().catch(() => "");

    let body: unknown = null;

    if (text) {
        try {
            body = JSON.parse(text);
        } catch {
            body = text;
        }
    }

    const message =
        typeof body === "object" && body !== null && "message" in body
            ? String((body as { message?: string }).message ?? fallbackMessage)
            : fallbackMessage;

    const error = new Error(message) as ApiError;
    error.status = response.status;
    error.details = body;

    return error;
}

async function refreshAccessToken(): Promise<string | null> {
    if (!refreshPromise) {
        refreshPromise = (async () => {
            const refreshToken = TokenStore.getRefresh();

            if (!refreshToken) {
                return null;
            }

            const response = await fetch(buildUrl(`${AUTH_PATH}/refresh`), {
                method: "POST",
                credentials: "include",
                headers: {
                    Authorization: `Bearer ${refreshToken}`,
                },
            });

            if (!response.ok) {
                TokenStore.clear();
                return null;
            }

            const data = (await response.json()) as {
                authToken?: string;
                refreshToken?: string;
            };

            if (!data.authToken) {
                TokenStore.clear();
                return null;
            }

            TokenStore.setAccess(data.authToken);
            TokenStore.setRefresh(data.refreshToken ?? null);

            return data.authToken;
        })().finally(() => {
            refreshPromise = null;
        });
    }

    return refreshPromise;
}

export async function apiFetch(path: string, init: ApiRequestInit = {}): Promise<Response> {
    const { _retry = false, skipAuthRefresh = false, headers, ...rest } = init;
    const accessToken = TokenStore.getAccess();

    const response = await fetch(buildUrl(path), {
        ...rest,
        credentials: "include",
        headers: {
            ...(accessToken ? { Authorization: `Bearer ${accessToken}` } : {}),
            ...(headers ?? {}),
        },
    });

    if (response.status !== 401 || skipAuthRefresh || _retry) {
        return response;
    }

    const nextAccessToken = await refreshAccessToken();

    if (!nextAccessToken) {
        return response;
    }

    return apiFetch(path, {
        ...rest,
        headers: {
            Authorization: `Bearer ${nextAccessToken}`,
            ...(headers ?? {}),
        },
        _retry: true,
    });
}

export async function apiRequest<T>(
    path: string,
    init: ApiRequestInit = {},
    fallbackMessage = "Erro na requisicao"
): Promise<T> {
    const response = await apiFetch(path, init);

    if (!response.ok) {
        throw await parseError(response, fallbackMessage);
    }

    if (response.status === 204) {
        return undefined as T;
    }

    return (await response.json()) as T;
}
