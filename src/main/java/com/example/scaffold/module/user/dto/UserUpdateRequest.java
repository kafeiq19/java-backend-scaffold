package com.example.scaffold.module.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(max = 64, message = "name must be at most 64 characters")
        String name,

        @Email(message = "email must be valid")
        @Size(max = 128, message = "email must be at most 128 characters")
        String email
) {
}
