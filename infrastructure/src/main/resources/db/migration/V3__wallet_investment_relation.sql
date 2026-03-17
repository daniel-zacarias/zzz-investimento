ALTER TABLE investments
    ADD COLUMN wallet_id VARCHAR(36);

ALTER TABLE investments
    ALTER COLUMN wallet_id SET NOT NULL;

ALTER TABLE investments
    ADD CONSTRAINT fk_investments_wallets
        FOREIGN KEY (wallet_id) REFERENCES wallets(id);

CREATE INDEX IF NOT EXISTS idx_investments_wallet_id ON investments(wallet_id);