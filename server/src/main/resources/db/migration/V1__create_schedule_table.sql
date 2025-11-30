CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE schedule (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          device_id UUID NOT NULL,
                          start_time TIMESTAMP NOT NULL,
                          end_time TIMESTAMP NOT NULL
);