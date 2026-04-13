import { Wallet, CreditCard, TrendingUp, type LucideIcon } from "lucide-react";
import type { AccountType } from "@/lib/types/account.types";

/**
 * Mapa de ícones por tipo de conta
 */
export const ACCOUNT_ICON_MAP: Record<AccountType, LucideIcon> = {
    CHECKING_ACCOUNT: Wallet,
    CREDIT_CARD: CreditCard,
    INVESTMENT_ACCOUNT: TrendingUp,
};

/**
 * Mapa de cores por tipo de conta
 */
export const ACCOUNT_COLOR_MAP: Record<AccountType, string> = {
    CHECKING_ACCOUNT: "text-blue-500",
    CREDIT_CARD: "text-purple-500",
    INVESTMENT_ACCOUNT: "text-green-500",
};

/**
 * Mapa de labels por tipo de conta
 */
export const ACCOUNT_LABEL_MAP: Record<AccountType, string> = {
    CHECKING_ACCOUNT: "Conta Corrente",
    CREDIT_CARD: "Cartão de Crédito",
    INVESTMENT_ACCOUNT: "Investimentos",
};

/**
 * Tipos de conta para formulário
 */
export const ACCOUNT_TYPES = [
    { value: "CHECKING_ACCOUNT", label: "Conta Corrente" },
    { value: "CREDIT_CARD", label: "Cartão de Crédito" },
    { value: "INVESTMENT_ACCOUNT", label: "Investimentos" },
] as const;