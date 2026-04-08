ALTER TABLE wallets
    ADD COLUMN IF NOT EXISTS name VARCHAR(255) NOT NULL DEFAULT 'Geral';

ALTER TABLE wallets
    DROP CONSTRAINT IF EXISTS wallets_user_id_key;

CREATE INDEX IF NOT EXISTS idx_wallets_user_id ON wallets(user_id);

CREATE UNIQUE INDEX IF NOT EXISTS uk_wallets_user_id_name ON wallets(user_id, name);
