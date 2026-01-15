CREATE TABLE device_request (
                                id UUID PRIMARY KEY,
                                user_id UUID NOT NULL,
                                request_type VARCHAR(20) NOT NULL,
                                status VARCHAR(20) NOT NULL,
                                created_at TIMESTAMP,


                                device_name VARCHAR(255),
                                device_type VARCHAR(50),
                                area DOUBLE PRECISION,
                                max_power INT,
                                min_wind INT,

                                target_device_id UUID
);