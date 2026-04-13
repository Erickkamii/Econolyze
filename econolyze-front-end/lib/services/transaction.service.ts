import type {
    CreateTransactionPayload,
    TransactionResponse,
    Transaction,
} from "@/lib/types/transaction.types";
import type { DependenciesResponse } from "@/lib/types/account.types";

const API_BASE = process.env.NEXT_PUBLIC_API_BASE_URL?.replace(/\/+$/, "") ?? "";


export class TransactionService {
    private static readonly TRANSACTION_PATH = "/transaction";
    private static readonly DEPENDENCIES_PATH = "/dependencies";

    static async create(
        payload: CreateTransactionPayload,
        accessToken: string | null
    ): Promise<TransactionResponse> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }
        const url = `${API_BASE}${this.TRANSACTION_PATH}`;

        const response = await fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${accessToken}`,
            },
            body: JSON.stringify(payload),
        });

        if (!response.ok) {
            const body = await response.json().catch(() => null);
            const error: any = new Error(
                body?.message ?? "Erro ao registrar transação"
            );
            error.status = response.status;
            error.details = body;
            throw error;
        }

        return await response.json();
    }

    static async getDependencies(
        accessToken: string | null
    ): Promise<DependenciesResponse> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }
        const url = `${API_BASE}${this.DEPENDENCIES_PATH}`;

        const response = await fetch(url, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            const body = await response.json().catch(() => null);
            const error: any = new Error(
                body?.message ?? "Erro ao carregar dependências"
            );
            error.status = response.status;
            error.details = body;
            throw error;
        }

        return await response.json();
    }

    static async getById(
        id: number,
        accessToken: string | null
    ): Promise<Transaction> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        const url = `${API_BASE}${this.TRANSACTION_PATH}/${id}`;

        const response = await fetch(url, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            const body = await response.json().catch(() => null);
            const error: any = new Error(
                body?.message ?? "Erro ao buscar transação"
            );
            error.status = response.status;
            error.details = body;
            throw error;
        }

        return await response.json();
    }

    static async getAll(
        accessToken: string | null,
        page: number = 0,
        pageSize: number = 20,
        type?: "INCOME" | "EXPENSE"
    ): Promise<Transaction[]> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        let url = `${API_BASE}${this.TRANSACTION_PATH}?page=${page}&pageSize=${pageSize}`;

        if (type) {
            url += `&type=${type}`;
        }

        const response = await fetch(url, {
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            const body = await response.json().catch(() => null);
            const error: any = new Error(
                body?.message ?? "Erro ao buscar transações"
            );
            error.status = response.status;
            error.details = body;
            throw error;
        }

        const data = await response.json();

        return data.content ?? [];
    }

    static async delete(
        id: number,
        accessToken: string | null
    ): Promise<void> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        const url = `${API_BASE}${this.TRANSACTION_PATH}/${id}`;

        const response = await fetch(url, {
            method: "DELETE",
            headers: {
                Authorization: `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            const body = await response.json().catch(() => null);
            const error: any = new Error(
                body?.message ?? "Erro ao deletar transação"
            );
            error.status = response.status;
            error.details = body;
            throw error;
        }
    }
}