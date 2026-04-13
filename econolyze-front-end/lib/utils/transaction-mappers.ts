import type {
    TransactionCategory,
    TransactionType,
} from "@/lib/types/transaction.types";

/**
 * Mapeia categoria do formulário para o backend
 */
export function mapCategoryToBackend(value: string): TransactionCategory {
    switch (value) {
        case "alimentacao":
            return "FOOD";
        case "transporte":
            return "TRANSPORT";
        case "lazer":
        case "compras":
        case "salario":
        case "freelance":
            return "OTHER";
        case "contas":
            return "UTILITIES";
        default:
            return "OTHER";
    }
}

/**
 * Mapeia tipo de transação para o backend
 */
export function mapTypeToBackend(tipo: "receita" | "gasto"): TransactionType {
    return tipo === "receita" ? "INCOME" : "EXPENSE";
}

/**
 * Mapeia ID de conta (string -> number | null)
 */
export function mapAccountId(value: string): number | null {
    const id = Number(value);
    return Number.isNaN(id) ? null : id;
}

/**
 * Verifica se uma transação é receita
 */
export function isIncomeTransaction(type: TransactionType | null): boolean {
    return ["INCOME", "REFUND", "INVESTMENT"].includes(type ?? "");
}

/**
 * Formata valor monetário para exibição
 */
export function formatCurrency(value: number): string {
    return value.toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    });
}