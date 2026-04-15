import type {
    Account,
    CreateAccountPayload,
    UpdateAccountPayload,
    AccountResponse,
} from "@/lib/types/account.types";
import { apiRequest } from "@/lib/services/api-client";

export class AccountService {
    private static readonly ACCOUNT_PATH = "/account";

    private static async request<T>(
        path: string,
        options: RequestInit,
        accessToken: string | null
    ): Promise<T> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        return apiRequest<T>(path, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {}),
            },
        });
    }

    static getAll(accessToken: string | null) {
        return this.request<Account[]>(
            this.ACCOUNT_PATH,
            { method: "GET" },
            accessToken
        );
    }

    static getById(id: number, accessToken: string | null) {
        return this.request<Account>(
            `${this.ACCOUNT_PATH}/${id}`,
            { method: "GET" },
            accessToken
        );
    }

    static create(
        payload: CreateAccountPayload,
        accessToken: string | null
    ) {
        return this.request<AccountResponse>(
            this.ACCOUNT_PATH,
            {
                method: "POST",
                body: JSON.stringify(payload),
            },
            accessToken
        );
    }

    static update(
        id: number,
        payload: UpdateAccountPayload,
        accessToken: string | null
    ) {
        return this.request<AccountResponse>(
            `${this.ACCOUNT_PATH}/${id}`,
            {
                method: "PUT",
                body: JSON.stringify(payload),
            },
            accessToken
        );
    }

    static delete(id: number, accessToken: string | null) {
        return this.request<void>(
            `${this.ACCOUNT_PATH}/${id}`,
            { method: "DELETE" },
            accessToken
        );
    }
}
