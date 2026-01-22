ALTER TABLE energy_measure
    DROP CONSTRAINT "fk_energymeasure_on_device";

ALTER TABLE energy_measure
    ADD CONSTRAINT "fk_energymeasure_on_device"
        FOREIGN KEY (device_id)
            REFERENCES device (id)
            ON DELETE CASCADE;