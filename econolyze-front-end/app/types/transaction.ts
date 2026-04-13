
// --- 1. ENUMS (Literais) ---
type TransactionCategory = 'FOOD' | 'HOUSEHOLD' | 'TRANSPORT' | 'HEALTH' | 'INSURANCE' | 'UTILITIES' | 'OTHER';
type TransactionTypeLiteral = 'INCOME' | 'EXPENSE' | 'TRANSFER' | 'SAVINGS' | 'REFUND' | 'INVESTMENT';
type TransactionStatus = 'PAID' | 'PAID_PARTIALLY' | 'PENDING' | 'CANCELLED';

// --- 2. DTO da Transação ---
type TransactionDTO = {
    id: number;
    amount: number;
    category: TransactionCategory; // Usando o Enum literal
    type: TransactionTypeLiteral; // Usando o Enum literal
    description: string;
    date: string; // Formato ISO 8601 (e.g., "2022-03-10")
    method: string | null;
    financialGoalId: number | null;
    isRecurring: boolean | null;
    initialPayment: number | null;
    paidAmount: number;
    remainingBalance: number;
    status: TransactionStatus | null;
    accountId: number | null;
    payments: any[]; // Se for complexo, pode ser definido separadamente
    recurringTemplateId: number | null;
};

// --- 3. DTO de Resposta Paginada (Retorno da API) ---
type PaginatedResponse = {
    content: TransactionDTO[];
    page: number;
    size: number;
    totalElements: number;
    totalPages: number;
};