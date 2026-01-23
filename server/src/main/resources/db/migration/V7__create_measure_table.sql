CREATE TABLE energy_measure
(
    id        UUID        NOT NULL,
    type      VARCHAR(31) NOT NULL,
    timestamp TIMESTAMP WITHOUT TIME ZONE,
    value     DECIMAL,
    device_id UUID,
    CONSTRAINT pk_energymeasure PRIMARY KEY (id)
);

ALTER TABLE energy_measure
    ADD CONSTRAINT FK_ENERGYMEASURE_ON_DEVICE FOREIGN KEY (device_id) REFERENCES device (id);

