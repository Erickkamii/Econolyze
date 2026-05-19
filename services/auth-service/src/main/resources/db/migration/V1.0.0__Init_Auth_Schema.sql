CREATE SCHEMA IF NOT EXISTS authentication;

-- 1. Users Table
CREATE TABLE IF NOT EXISTS authentication.users (
                                          id BIGSERIAL PRIMARY KEY,
                                          username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(255) NOT NULL
    );

-- 2. Revoked Tokens Table (Blocklist para JWT)
CREATE TABLE IF NOT EXISTS authentication.revoked_tokens (
                                                   id BIGSERIAL PRIMARY KEY,
                                                   jti VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMPTZ NOT NULL
    );

-- Index para otimizar a limpeza de tokens expirados
CREATE INDEX IF NOT EXISTS idx_revoked_tokens_expires_at ON authentication.revoked_tokens (expires_at);