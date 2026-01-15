CREATE TABLE password_reset_token
(
    id         UUID         NOT NULL,
    version    BIGINT,
    token      VARCHAR(255) NOT NULL,
    user_id    UUID,
    expires_at TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_passwordresettoken PRIMARY KEY (id)
);

ALTER TABLE password_reset_token
    ADD CONSTRAINT uc_passwordresettoken_token UNIQUE (token);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE energy_measure
    ADD CONSTRAINT FK_ENERGYMEASURE_ON_DEVICE FOREIGN KEY (device_id) REFERENCES device (id);

ALTER TABLE password_reset_token
    ADD CONSTRAINT FK_PASSWORDRESETTOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE conversation_participants
    DROP CONSTRAINT pk_conversation_participants;

ALTER TABLE forecasts
    DROP COLUMN forecasted_usage;

ALTER TABLE forecasts
    ADD forecasted_usage DOUBLE PRECISION NOT NULL;

ALTER TABLE device
    ALTER COLUMN total_consumed TYPE DECIMAL USING (total_consumed::DECIMAL);

ALTER TABLE device
    ALTER COLUMN total_generated TYPE DECIMAL USING (total_generated::DECIMAL);

ALTER TABLE energy_measure
    DROP COLUMN value;

ALTER TABLE energy_measure
    ADD value DOUBLE PRECISION;