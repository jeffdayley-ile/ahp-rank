-- TODO: Use postgres and keep data around
-- Clean up data before starting, always in a clean state
DROP TABLE options_criterion IF EXISTS;
DROP TABLE options IF EXISTS;
DROP TABLE criteria IF EXISTS;
DROP TABLE analysis IF EXISTS;

-- Create the Tables
CREATE TABLE analysis (
    id          UUID PRIMARY KEY
);

CREATE TABLE options (
    id          UUID PRIMARY KEY,
    analysis_id UUID REFERENCES analysis,
    name        VARCHAR NOT NULL,
    rank        INTEGER,
    total_score FLOAT NOT NULL -- Sum of OptionsCriterion Scores
);

CREATE TABLE criteria (
    id          UUID PRIMARY KEY,
    analysis_id UUID REFERENCES analysis,
    name        VARCHAR NOT NULL,
    rank        INTEGER NOT NULL,
    score       FLOAT NOT NULL
);

CREATE TABLE options_criterion (
    id              UUID PRIMARY KEY,
    analysis_id     UUID REFERENCES analysis,
    options_id      UUID REFERENCES options,
    criterion_id    UUID REFERENCES criteria,
    rank            INTEGER NOT NULL,
    score           FLOAT NOT NULL
);
