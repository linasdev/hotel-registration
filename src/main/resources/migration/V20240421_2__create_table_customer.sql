CREATE TABLE IF NOT EXISTS customer (
    id integer PRIMARY KEY,
    first_name varchar(256) NOT NULL,
    last_name varchar(256) NOT NULL,
    room_id boolean NOT NULL,
    booking_timestamp datetime NOT NULL,
    check_out_timestamp datetime DEFAULT NULL
);

CREATE INDEX IF NOT EXISTS idx__customer__room_id ON customer (room_id);
CREATE INDEX IF NOT EXISTS idx__customer__check_out_timestamp ON customer (check_out_timestamp);
