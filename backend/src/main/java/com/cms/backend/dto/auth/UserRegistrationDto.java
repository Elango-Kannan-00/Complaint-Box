package com.cms.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDto {
    /**
     * DTO used during user registration. Includes username, email, password and academic department id.
     */

    @NotBlank(message = "Username is required")
    private String userName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String userEmail;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must contain at least 8 characters")
    private String userPassword;

    @NotNull(message = "Department is required")
    private Long academicDepartmentId;
}