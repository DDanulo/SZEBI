CREATE TABLE announcements
(
    id        SERIAL        NOT NULL,
    version   BIGINT,
    author_id UUID          NOT NULL,
    content   VARCHAR(1000) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    level     VARCHAR(255)  NOT NULL,
    building  VARCHAR(255),
    status    VARCHAR(255)  NOT NULL,
    CONSTRAINT pk_announcements PRIMARY KEY (id)
);

ALTER TABLE announcements
    ADD CONSTRAINT FK_ANNOUNCEMENTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);
