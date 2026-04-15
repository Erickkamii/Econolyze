import type {
    TransactionCategory,
    TransactionType,
} from "@/lib/types/transaction.types";

export function mapCategoryToBackend(value: string): TransactionCategory {
    switch (value) {
        case "alimentacao":
            return "FOOD";
        case "transporte":
            return "TRANSPORT";
        case "moradia":
            return "HOUSEHOLD";
        case "saude":
            return "HEALTH";
        case "seguros":
            return "INSURANCE";
        case "contas":
            return "UTILITIES";
        case "lazer":
            return "LEISURE";
        case "compras":
        case "salario":
        case "freelance":
        default:
            return "OTHER";
    }
}

export function mapCategoryFromBackend(category: TransactionCategory | null | undefined): string {
    switch (category) {
        case "FOOD":
            return "alimentacao";
        case "TRANSPORT":
            return "transporte";
        case "HOUSEHOLD":
            return "moradia";
        case "HEALTH":
            return "saude";
        case "INSURANCE":
            return "seguros";
        case "UTILITIES":
            return "contas";
        case "OTHER":
        default:
            return "compras";
    }
}

export function mapTypeToBackend(tipo: "receita" | "gasto"): TransactionType {
    return tipo === "receita" ? "INCOME" : "EXPENSE";
}

export function mapTypeFromBackend(type: TransactionType | null | undefined): "receita" | "gasto" {
    return type === "INCOME" || type === "REFUND" || type === "INVESTMENT"
        ? "receita"
        : "gasto";
}

export function mapAccountId(value: string): number | null {
    const id = Number(value);
    return Number.isNaN(id) ? null : id;
}

export function isIncomeTransaction(type: TransactionType | null): boolean {
    return ["INCOME", "REFUND", "INVESTMENT"].includes(type ?? "");
}

export function formatCurrency(value: number): string {
    return value.toLocaleString("pt-BR", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2,
    });
}

export function toCurrencyInputValue(value: number | string | null | undefined): string {
    const normalized = typeof value === "number" ? value : Number(value ?? 0);
    return String(Math.round(normalized * 100));
}
