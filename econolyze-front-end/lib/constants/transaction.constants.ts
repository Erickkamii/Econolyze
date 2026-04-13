import {
    Coffee,
    Zap,
    Briefcase,
    Minus,
    Wallet,
    Shield,
    Heart,
    type LucideIcon,
} from "lucide-react";
import {TransactionCategory} from "@/lib/types/transaction.types";

/**
 * Mapa de ícones por categoria
 */
export const TRANSACTION_ICON_MAP: Record<TransactionCategory, LucideIcon> = {
    FOOD: Coffee,
    HOUSEHOLD: Wallet,
    TRANSPORT: Zap,
    HEALTH: Heart,
    INSURANCE: Shield,
    UTILITIES: Briefcase,
    OTHER: Minus,
};

/**
 * Mapa de labels por categoria
 */
export const TRANSACTION_LABEL_MAP: Record<TransactionCategory, string> = {
    FOOD: "Alimentação",
    HOUSEHOLD: "Casa/Moradia",
    TRANSPORT: "Transporte",
    HEALTH: "Saúde",
    INSURANCE: "Seguros",
    UTILITIES: "Contas e Serviços",
    OTHER: "Outras Despesas",
};

/**
 * Categorias do formulário (UI)
 */
export const FORM_CATEGORIES = [
    { value: "alimentacao", label: "🍽️ Alimentação" },
    { value: "transporte", label: "🚗 Transporte" },
    { value: "lazer", label: "🎮 Lazer" },
    { value: "compras", label: "🛍️ Compras" },
    { value: "contas", label: "💡 Contas" },
    { value: "salario", label: "💼 Salário" },
    { value: "freelance", label: "💻 Freelance" },
] as const;

/**
 * Métodos de pagamento
 */
export const PAYMENT_METHODS = [
    { value: "PIX", label: "PIX" },
    { value: "CREDIT_CARD", label: "Cartão de Crédito" },
    { value: "DEBIT_CARD", label: "Cartão de Débito" },
    { value: "CASH", label: "Dinheiro" },
] as const;