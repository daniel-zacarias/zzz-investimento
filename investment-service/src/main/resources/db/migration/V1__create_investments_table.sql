CREATE TABLE IF NOT EXISTS investments(
  id VARCHAR(36) PRIMARY KEY,
  wallet_id VARCHAR(36) NOT NULL,
  amount DOUBLE PRECISION NOT NULL,
  annual_rate DOUBLE PRECISION NOT NULL,
  month_amount DOUBLE PRECISION NOT NULL DEFAULT 0,
  result DOUBLE PRECISION NOT NULL,
  annual_period INTEGER NOT NULL,
  created_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
  updated_at TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL,
  deleted_at TIMESTAMP (6) WITHOUT TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_investments_wallet_id ON investments(wallet_id);
CREATE INDEX IF NOT EXISTS idx_investments_deleted_at ON investments(deleted_at);
