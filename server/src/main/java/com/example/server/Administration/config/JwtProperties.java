package com.example.server.Administration.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    private String secret;
    private Expiration expiration;

    @Getter
    @Setter
    public static class Expiration {
        private long access;
        private long refresh;
    }
}
