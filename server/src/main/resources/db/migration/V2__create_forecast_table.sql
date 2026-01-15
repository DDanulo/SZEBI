CREATE TABLE forecasts
(
    id               UUID    NOT NULL,
    creation_time    TIMESTAMP WITHOUT TIME ZONE,
    forecasted_usage DECIMAL NOT NULL,
    forecast_date    TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_forecasts PRIMARY KEY (id)
);