CREATE TABLE IF NOT EXISTS analysis (
    id          UUID PRIMARY KEY,
    created_at  DATETIME DEFAULT now()
);
