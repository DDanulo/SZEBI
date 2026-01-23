package com.example.server.Administration.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO (
        @NotBlank String login,

        @NotBlank String password)
{ }
