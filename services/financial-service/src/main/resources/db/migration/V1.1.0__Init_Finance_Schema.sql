CREATE SCHEMA IF NOT EXISTS finance;

-- 1. Accounts
CREATE TABLE IF NOT EXISTS finance.accounts (
                                                id BIGSERIAL PRIMARY KEY,
                                                user_id BIGINT NOT NULL, -- Apenas referência lógica
                                                name VARCHAR(255) NOT NULL,
    type VARCHAR(50),
    actual_balance NUMERIC(19,4) DEFAULT 0.0000,
    credit_limit NUMERIC(19,4) DEFAULT 0.0000,
    closing_date INTEGER,
    active BOOLEAN DEFAULT TRUE
    );

-- 2. Balance Snapshot (Para consultas rápidas de saldo e controle de concorrência)
CREATE TABLE IF NOT EXISTS finance.balance (
                                               user_id BIGINT PRIMARY KEY,
                                               balance NUMERIC(19,4) DEFAULT 0.0000,
    date DATE,
    income NUMERIC(19,4) DEFAULT 0.0000,
    expenses NUMERIC(19,4) DEFAULT 0.0000,
    version BIGINT NOT NULL DEFAULT 0 -- Mapeado para @Version do Hibernate (Optimistic Locking)
    );

-- 3. Financial Goals
CREATE TABLE IF NOT EXISTS finance.financial_goal (
                                                      id BIGSERIAL PRIMARY KEY,
                                                      user_id BIGINT NOT NULL,
                                                      name VARCHAR(255) NOT NULL,
    amount NUMERIC(19,4) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(50) DEFAULT 'ACTIVE'
    CHECK (status IN ('ACTIVE','COMPLETED','CANCELLED','PAUSED','OVERDUE','DRAFT')),
    type VARCHAR(50)
    CHECK (type IN ('VACATION','TRAVEL','INVESTMENT','SAVING','OTHER'))
    );

-- 4. Recurrency Templates
CREATE TABLE IF NOT EXISTS finance.recurrency_template (
                                                           id BIGSERIAL PRIMARY KEY,
                                                           user_id BIGINT NOT NULL,
                                                           amount NUMERIC(19,4) NOT NULL,
    type VARCHAR(50),
    category VARCHAR(50)
    CHECK (category IN ('FOOD','HOUSEHOLD','TRANSPORT','HEALTH','INSURANCE','UTILITIES','LEISURE','OTHER')),
    method VARCHAR(50)
    CHECK (method IN ('CREDIT_CARD','CASH','BANK_TRANSFER','PAYPAL','PIX','DEBIT_CARD')),
    description VARCHAR(255),
    frequency VARCHAR(50),
    start_date DATE NOT NULL,
    end_date DATE,
    max_occurrences INTEGER,
    next_occurrence DATE,
    times_processed INTEGER DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE
    );

-- 5. Transactions
CREATE TABLE IF NOT EXISTS finance.transactions (
                                                    id BIGSERIAL PRIMARY KEY,
                                                    user_id BIGINT NOT NULL,
                                                    account_id BIGINT,
                                                    financial_goal_id BIGINT,
                                                    recurring_template_id BIGINT,
                                                    amount NUMERIC(19,4) NOT NULL,
    category VARCHAR(50),
    type VARCHAR(50),
    method VARCHAR(50)
    CHECK (method IN ('CREDIT_CARD','CASH','BANK_TRANSFER','PAYPAL','PIX','DEBIT_CARD')),
    status VARCHAR(50) DEFAULT 'PENDING'
    CHECK (status IN ('PAID','PAID_PARTIALLY','PENDING','CANCELLED')),
    is_recurring BOOLEAN DEFAULT FALSE,
    description VARCHAR(255),
    date DATE NOT NULL,

    CONSTRAINT fk_tx_account FOREIGN KEY (account_id) REFERENCES finance.accounts (id) ON DELETE SET NULL,
    CONSTRAINT fk_tx_goal FOREIGN KEY (financial_goal_id) REFERENCES finance.financial_goal (id) ON DELETE SET NULL,
    CONSTRAINT fk_tx_recurring FOREIGN KEY (recurring_template_id) REFERENCES finance.recurrency_template (id) ON DELETE SET NULL
    );

-- 6. Payments
CREATE TABLE IF NOT EXISTS finance.payments (
                                                id BIGSERIAL PRIMARY KEY,
                                                transaction_id BIGINT NOT NULL,
                                                account_id BIGINT,
                                                amount NUMERIC(19,4) NOT NULL,
    method VARCHAR(50),
    paid_at DATE,
    status VARCHAR(50) DEFAULT 'COMPLETED',
    description VARCHAR(255),

    CONSTRAINT fk_payments_transaction FOREIGN KEY (transaction_id) REFERENCES finance.transactions (id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_account FOREIGN KEY (account_id) REFERENCES finance.accounts (id) ON DELETE SET NULL
    );

-- 7. Monthly Budget
CREATE TABLE IF NOT EXISTS finance.monthly_budget (
                                                      id BIGSERIAL PRIMARY KEY,
                                                      user_id BIGINT NOT NULL,
                                                      amount NUMERIC(19,4) NOT NULL
    );

-- Índices Críticos para Performance de Busca
CREATE INDEX IF NOT EXISTS idx_transactions_user_date ON finance.transactions (user_id, date);
CREATE INDEX IF NOT EXISTS idx_payments_transaction ON finance.payments (transaction_id);
CREATE INDEX IF NOT EXISTS idx_accounts_user_id ON finance.accounts (user_id);