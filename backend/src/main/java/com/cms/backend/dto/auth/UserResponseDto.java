package com.cms.backend.dto.auth;

import com.cms.backend.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    /**
     * DTO returned to clients with public user information (id, name, email, role).
     */

    private Long userId;
    private String userName;
    private String userEmail;
    private UserRole userRole;
}
