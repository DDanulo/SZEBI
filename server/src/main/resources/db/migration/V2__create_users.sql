CREATE TABLE device
(
    id                           UUID        NOT NULL,
    type                         VARCHAR(31) NOT NULL,
    working                      BOOLEAN,
    total_generated              DECIMAL,
    description                  VARCHAR(255),
    total_consumed               DECIMAL,
    max_power_per_hour           INTEGER,
    min_wind_speed_for_max_power INTEGER,
    area                         DOUBLE PRECISION,
    CONSTRAINT pk_device PRIMARY KEY (id)
);

CREATE TABLE users
(
    id           UUID        NOT NULL,
    type         VARCHAR(31) NOT NULL,
    version      BIGINT,
    login        VARCHAR(255),
    passwordhash VARCHAR(255),
    firstname    VARCHAR(255),
    lastname     VARCHAR(255),
    active       BOOLEAN,
    email        VARCHAR(255),
    room         VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_users_login UNIQUE (login);