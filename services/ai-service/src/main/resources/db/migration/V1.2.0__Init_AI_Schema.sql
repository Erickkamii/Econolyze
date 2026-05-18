CREATE EXTENSION IF NOT EXISTS vector;

CREATE SCHEMA IF NOT EXISTS ai;

-- 1. Transaction Embeddings
CREATE TABLE IF NOT EXISTS ai.transaction_embedding (
                                                        embedding_id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    embedding VECTOR(768),
    text TEXT,
    metadata JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
    );

-- 2. Conversation Embeddings
CREATE TABLE IF NOT EXISTS ai.conversation_embedding (
                                                         embedding_id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    embedding VECTOR(768),
    text TEXT,
    metadata JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
    );

-- 3. Profile Embeddings
CREATE TABLE IF NOT EXISTS ai.profile_embedding (
                                                    embedding_id UUID NOT NULL DEFAULT gen_random_uuid() PRIMARY KEY,
    embedding VECTOR(768),
    text TEXT,
    metadata JSONB,
    created_at TIMESTAMPTZ DEFAULT NOW()
    );

-- Índices GIN para buscas rápidas dentro do campo de metadados JSONB
CREATE INDEX IF NOT EXISTS idx_ai_transaction_metadata ON ai.transaction_embedding USING GIN (metadata);
CREATE INDEX IF NOT EXISTS idx_ai_conversation_metadata ON ai.conversation_embedding USING GIN (metadata);
CREATE INDEX IF NOT EXISTS idx_ai_profile_metadata ON ai.profile_embedding USING GIN (metadata);