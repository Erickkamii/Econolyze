import type {
    RecurringTemplate,
    CreateRecurringPayload,
    UpdateRecurringPayload,
} from "@/lib/types/recurring.types"
import { apiRequest } from "@/lib/services/api-client"

export class RecurringService {
    private static readonly PATH = "/financial/recurring"

    private static async request<T>(
        path: string,
        options: RequestInit,
        accessToken: string | null
    ): Promise<T> {
        if (!accessToken) throw new Error("Access token is required")

        return apiRequest<T>(path, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                ...(options.headers || {}),
            },
        })
    }

    static getAll(accessToken: string | null) {
        return this.request<RecurringTemplate[]>(
            this.PATH,
            { method: "GET" },
            accessToken
        )
    }

    static getById(id: number, accessToken: string | null) {
        return this.request<RecurringTemplate>(
            `${this.PATH}/${id}`,
            { method: "GET" },
            accessToken
        )
    }

    static create(payload: CreateRecurringPayload, accessToken: string | null) {
        return this.request<RecurringTemplate>(
            this.PATH,
            { method: "POST", body: JSON.stringify(payload) },
            accessToken
        )
    }

    static update(id: number, payload: UpdateRecurringPayload, accessToken: string | null) {
        return this.request<RecurringTemplate>(
            `${this.PATH}/${id}`,
            { method: "PUT", body: JSON.stringify(payload) },
            accessToken
        )
    }

    static toggle(id: number, accessToken: string | null) {
        return this.request<RecurringTemplate>(
            `${this.PATH}/${id}/toggle`,
            { method: "PATCH" },
            accessToken
        )
    }

    static delete(id: number, accessToken: string | null) {
        return this.request<void>(
            `${this.PATH}/${id}`,
            { method: "DELETE" },
            accessToken
        )
    }
}
