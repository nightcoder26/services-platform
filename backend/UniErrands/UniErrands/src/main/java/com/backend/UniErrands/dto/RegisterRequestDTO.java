package com.backend.UniErrands.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class RegisterRequestDTO {
     @NotBlank(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "REQUESTER|HELPER|BOTH|ADMIN", message = "Role must be REQUESTER, HELPER, BOTH, or ADMIN")
    private String role;
}
