/**
 * Categorias de transacao
 */
export type TransactionCategory =
    | "FOOD"
    | "HOUSEHOLD"
    | "TRANSPORT"
    | "HEALTH"
    | "INSURANCE"
    | "UTILITIES"
    | "LEISURE"
    | "OTHER";

/**
 * Tipos de transacao
 */
export type TransactionType = "INCOME" | "EXPENSE" | "REFUND" | "INVESTMENT";

/**
 * Metodos de pagamento
 */
export type PaymentMethod = "PIX" | "CREDIT_CARD" | "DEBIT_CARD" | "CASH";

/**
 * Status de pagamento da transacao
 */
export type TransactionStatus = "PAID" | "PAID_PARTIALLY" | "PENDING" | "CANCELLED";

export type PaymentResponse = {
    transactionId: number;
    amount: number | string | null;
    method: string | null;
    status: string | null;
    paidAt: string | null;
    description: string | null;
};

/**
 * Transacao do backend
 */
export type Transaction = {
    id: number;
    amount: number | string | null;
    category: TransactionCategory | null;
    type: TransactionType | null;
    description: string | null;
    date?: string;
    accountId?: number | null;
    financialGoalId?: number | null;
    method?: PaymentMethod | null;
    isRecurring?: boolean | null;
    initialPayment?: number | null;
    paidAmount?: number | string | null;
    remainingBalance?: number | string | null;
    status?: TransactionStatus | null;
    payments?: PaymentResponse[] | null;
    recurringTemplateId?: number | null;
};

export type TransactionPayload = {
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
 * Payload para criar transacao
 */
export type CreateTransactionPayload = TransactionPayload;

/**
 * Payload para atualizar transacao
 */
export type UpdateTransactionPayload = TransactionPayload;

/**
 * Resposta da API de transacoes
 */
export type TransactionResponse = {
    id: number;
    amount: number;
    category: TransactionCategory;
    type: TransactionType;
    description: string;
    date: string;
    method?: string | null;
    financialGoalId?: number | null;
    isRecurring?: boolean | null;
    initialPayment?: number | null;
    paidAmount?: number | string | null;
    remainingBalance?: number | string | null;
    status?: TransactionStatus | null;
    accountId?: number | null;
    payments?: PaymentResponse[] | null;
    recurringTemplateId?: number | null;
};
