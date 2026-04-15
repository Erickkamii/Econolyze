import type {
    CreateTransactionPayload,
    TransactionResponse,
    Transaction,
    UpdateTransactionPayload,
} from "@/lib/types/transaction.types";
import type { DependenciesResponse } from "@/lib/types/account.types";
import { apiRequest } from "@/lib/services/api-client";

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

        return apiRequest<TransactionResponse>(
            this.TRANSACTION_PATH,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            },
            "Erro ao registrar transacao"
        );
    }

    static async update(
        id: number,
        payload: UpdateTransactionPayload,
        accessToken: string | null
    ): Promise<TransactionResponse> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        return apiRequest<TransactionResponse>(
            `${this.TRANSACTION_PATH}/${id}`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify(payload),
            },
            "Erro ao atualizar transacao"
        );
    }

    static async getDependencies(
        accessToken: string | null
    ): Promise<DependenciesResponse> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        return apiRequest<DependenciesResponse>(
            this.DEPENDENCIES_PATH,
            {},
            "Erro ao carregar dependencias"
        );
    }

    static async getById(
        id: number,
        accessToken: string | null
    ): Promise<Transaction> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        return apiRequest<Transaction>(
            `${this.TRANSACTION_PATH}/${id}`,
            {},
            "Erro ao buscar transacao"
        );
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

        let url = `${this.TRANSACTION_PATH}?page=${page}&pageSize=${pageSize}`;

        if (type) {
            url += `&type=${type}`;
        }

        const data = await apiRequest<{ content?: Transaction[] }>(
            url,
            {},
            "Erro ao buscar transacoes"
        );

        return data.content ?? [];
    }

    static async delete(
        id: number,
        accessToken: string | null
    ): Promise<void> {
        if (!accessToken) {
            throw new Error("Access token is required");
        }

        await apiRequest<void>(
            `${this.TRANSACTION_PATH}/${id}`,
            {
                method: "DELETE",
            },
            "Erro ao deletar transacao"
        );
    }
}
