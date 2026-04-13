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
    private static async post<T>(path: string, body?: any): Promise<T> {
        const url = `${API_BASE}${AUTH_PATH}${path}`;

        const response = await fetch(url, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(body ?? {}),
        });

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

        return (await response.json()) as T;
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

        const data = await this.post<RefreshResponse>("/refresh", { refreshToken });

        if (data.authToken) TokenStore.setAccess(data.authToken);
        if (data.refreshToken) TokenStore.setRefresh(data.refreshToken);

        return data;
    }


    static async logout(): Promise<void> {
        const refreshToken = TokenStore.getRefresh();

        try {
            await this.post("/logout", { refreshToken });
        } catch (error) {
            console.warn("Logout API error:", error);
        }

        TokenStore.clear();
    }
}