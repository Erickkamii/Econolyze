/**
 * Categorias de transação
 */
export type TransactionCategory =
    | "FOOD"
    | "HOUSEHOLD"
    | "TRANSPORT"
    | "HEALTH"
    | "INSURANCE"
    | "UTILITIES"
    | "OTHER";

/**
 * Tipos de transação
 */
export type TransactionType = "INCOME" | "EXPENSE" | "REFUND" | "INVESTMENT";

/**
 * Métodos de pagamento
 */
export type PaymentMethod = "PIX" | "CREDIT_CARD" | "DEBIT_CARD" | "CASH";

/**
 * Transação do backend
 */
export type Transaction = {
    id: number;
    amount: number | string | null;
    category: TransactionCategory | null;
    type: TransactionType | null;
    description: string | null;
    date?: string;
    accountId?: number;
    financialGoalId?: number;
    method?: PaymentMethod;
};

/**
 * Payload para criar transação
 */
export type CreateTransactionPayload = {
    amount: number;
    category: TransactionCategory;
    type: TransactionType;
    date: string;
    description: string;
    accountId: number | null;
    financialGoalId: number | null;
    recurringTemplateId: number | null;
    isRecurring: boolean;
    method: PaymentMethod;
    initialPayment: number;
};

/**
 * Resposta da API de transações
 */
export type TransactionResponse = {
    id: number;
    amount: number;
    category: TransactionCategory;
    type: TransactionType;
    description: string;
    date: string;
};