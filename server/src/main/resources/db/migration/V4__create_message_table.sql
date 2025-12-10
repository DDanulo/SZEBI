CREATE TABLE messages (
    id UUID PRIMARY KEY,
    version BIGINT NOT NULL,
    content TEXT NOT NULL,
    author_login VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
