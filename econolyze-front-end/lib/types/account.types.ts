/**
 * Tipos de conta
 */
export type AccountType =
    | "CHECKING_ACCOUNT"
    | "SAVINGS_ACCOUNT"
    | "CREDIT_CARD"
    | "INVESTMENT_ACCOUNT"
    | "MONEY";

/**
 * Payload para criar conta
 */
export type CreateAccountPayload = {
    name: string;
    type: AccountType;
    actualBalance: number;
    creditLimit: number;
};

/**
 * Conta bancária
 */
export type Account = {
    id: number;
    name: string;
    type: AccountType;
    actualBalance: number;
    creditLimit: number;
};

/**
 * Resposta da API de conta
 */
export type AccountResponse = {
    id: number;
    name: string;
    type: AccountType;
    actualBalance: number;
    creditLimit: number;
};

export type UpdateAccountPayload = {
    name: string;
    type: AccountType;
    actualBalance: number;
    creditLimit: number;
};


/**
 * Status da meta
 */
export type GoalStatus = "ACTIVE" | "COMPLETED" | "CANCELLED" | "PAUSED" | "OVERDUE" | "DRAFT";

/**
 * Tipo de meta
 */
export type GoalType = "VACATION" | "TRAVEL" | "INVESTMENT" | "SAVING" | "OTHER";

/**
 * Meta financeira
 */
export type Goal = {
    id: number;
    name: string;
    amount: number;
    description: string;
    type: GoalType;
    status: GoalStatus;
};

export type CreateGoalPayload = {
    name: string;
    amount: number;
    description: string;
    type: GoalType;
    status: GoalStatus;
};

export type GoalProgress = {
    id: number;
    name: string;
    progress: number;
};

/**
 * Resposta das dependências
 */
export type DependenciesResponse = {
    accounts: Account[];
    goals: Goal[];
};
