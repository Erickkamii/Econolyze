import { TokenStore } from "@/lib/utils/token-store";
import type {
    LoginRequest,
    LoginResponse,
    RegisterRequest,
    RegisterResponse,
    RefreshResponse,
} from "@/lib/types/auth.types";

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/+$/, "") ?? "";
const AUTH_PATH = "/auth";


export class AuthService {
    private static async post<T>(
        path: string,
        body?: any,
        authToken?: string
    ): Promise<T> {
        const url = `${API_BASE}${AUTH_PATH}${path}`;
        const init: RequestInit = {
            method: "POST",
            credentials: "include",
            headers: {},
        };

        if (body !== undefined) {
            (init.headers as Record<string, string>)["Content-Type"] = "application/json";
            init.body = JSON.stringify(body);
        }

        if (authToken) {
            (init.headers as Record<string, string>)["Authorization"] =
                authToken.startsWith("Bearer ")
                    ? authToken
                    : `Bearer ${authToken}`;
        }

        const response = await fetch(url, init);

        if (!response.ok) {
            const text = await response.text();
            let json;

            try {
                json = JSON.parse(text);
            } catch {
                json = text;
            }

            const error: any = new Error(
                json?.message ?? "Erro na requisição de autenticação"
            );
            error.status = response.status;
            error.details = json;
            throw error;
        }

        if (response.status === 204 || response.status === 205) {
            return undefined as T;
        }

        const responseText = await response.text();
        return responseText ? (JSON.parse(responseText) as T) : (undefined as T);
    }

    static async login(payload: LoginRequest): Promise<LoginResponse> {
        const data = await this.post<LoginResponse>("/login", payload);

        if (data.authToken) TokenStore.setAccess(data.authToken);
        if (data.refreshToken) TokenStore.setRefresh(data.refreshToken);

        return data;
    }

    static async register(payload: RegisterRequest): Promise<RegisterResponse> {
        return await this.post<RegisterResponse>("/register", payload);
    }

    static async refresh(): Promise<RefreshResponse> {
        const refreshToken = TokenStore.getRefresh();

        if (!refreshToken) {
            throw new Error("No refresh token available");
        }

        const authorization = refreshToken.startsWith("Bearer ")
            ? refreshToken
            : `Bearer ${refreshToken}`;

        const data = await this.post<RefreshResponse>("/refresh", undefined, authorization);

        if (data.authToken) TokenStore.setAccess(data.authToken);
        if (data.refreshToken) TokenStore.setRefresh(data.refreshToken);

        return data;
    }


    static async logout(): Promise<void> {
        const refreshToken = TokenStore.getRefresh();

        if (!refreshToken) {
            TokenStore.clear();
            return;
        }

        const authorization = refreshToken.startsWith("Bearer ")
            ? refreshToken
            : `Bearer ${refreshToken}`;

        try {
            await this.post<void>("/logout", undefined, authorization);
        } catch (error) {
            console.warn("Logout API error:", error);
        } finally {
            TokenStore.clear();
        }
    }
}