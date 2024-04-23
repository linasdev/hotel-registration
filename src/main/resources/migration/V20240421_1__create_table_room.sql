CREATE TABLE IF NOT EXISTS room (
    id integer PRIMARY KEY,
    name varchar(256) NOT NULL,
    occupied boolean NOT NULL DEFAULT false,
    CONSTRAINT uq__room__name UNIQUE (name)
);

CREATE INDEX IF NOT EXISTS idx__room__occupied ON room (occupied);
