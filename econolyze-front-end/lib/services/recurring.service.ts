import type {
    RecurringTemplate,
    CreateRecurringPayload,
    UpdateRecurringPayload,
} from "@/lib/types/recurring.types"

const API_BASE =
    process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/+$/, "") ?? ""

export class RecurringService {
    private static readonly PATH = "/financial/recurring"

    private static async request<T>(
        path: string,
        options: RequestInit,
        accessToken: string | null
    ): Promise<T> {
        if (!accessToken) throw new Error("Access token is required")

        const response = await fetch(`${API_BASE}${path}`, {
            ...options,
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${accessToken}`,
                ...(options.headers || {}),
            },
        })

        if (!response.ok) {
            const body = await response.json().catch(() => null)
            const error: any = new Error(body?.message ?? "Erro na requisição")
            error.status = response.status
            error.details = body
            throw error
        }

        if (response.status === 204) return undefined as T

        return response.json()
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