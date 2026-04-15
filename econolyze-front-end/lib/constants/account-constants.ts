import {
    Wallet,
    CreditCard,
    TrendingUp,
    PiggyBank,
    Banknote,
    type LucideIcon,
} from "lucide-react";
import type { AccountType } from "@/lib/types/account.types";

export const ACCOUNT_ICON_MAP: Record<AccountType, LucideIcon> = {
    CHECKING_ACCOUNT: Wallet,
    SAVINGS_ACCOUNT: PiggyBank,
    CREDIT_CARD: CreditCard,
    INVESTMENT_ACCOUNT: TrendingUp,
    MONEY: Banknote,
};

export const ACCOUNT_COLOR_MAP: Record<AccountType, string> = {
    CHECKING_ACCOUNT: "text-blue-500",
    SAVINGS_ACCOUNT: "text-cyan-500",
    CREDIT_CARD: "text-purple-500",
    INVESTMENT_ACCOUNT: "text-green-500",
    MONEY: "text-amber-500",
};

export const ACCOUNT_LABEL_MAP: Record<AccountType, string> = {
    CHECKING_ACCOUNT: "Conta Corrente",
    SAVINGS_ACCOUNT: "Poupanca",
    CREDIT_CARD: "Cartao de Credito",
    INVESTMENT_ACCOUNT: "Investimentos",
    MONEY: "Dinheiro",
};

export const ACCOUNT_TYPES = [
    { value: "CHECKING_ACCOUNT", label: "Conta Corrente" },
    { value: "SAVINGS_ACCOUNT", label: "Poupanca" },
    { value: "CREDIT_CARD", label: "Cartao de Credito" },
    { value: "INVESTMENT_ACCOUNT", label: "Investimentos" },
    { value: "MONEY", label: "Dinheiro" },
] as const;
