package com.cms.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.repository.AcademicDepartmentRepository;
import com.cms.backend.entity.User;
import com.cms.backend.enums.UserRole;
import com.cms.backend.dto.auth.UserResponseDto;
import com.cms.backend.dto.auth.UserLoginDto;
import com.cms.backend.dto.auth.UserRegistrationDto;
import com.cms.backend.repository.UserRepository;

@Service
public class AuthService {
    /**
     * Service handling user registration and login operations.
     * Uses `UserRepository` for persistence and `BCryptPasswordEncoder` for password handling.
     */
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AcademicDepartmentRepository academicDepartmentRepository;

    public UserResponseDto register(UserRegistrationDto dto) {
        // check for existing user by email
        if (repository.existsByUserEmail(dto.getUserEmail())) {
            throw new RuntimeException("User Already Exist");
        }

        User user = new User();

        user.setUserName(dto.getUserName());
        user.setUserEmail(dto.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(dto.getUserPassword()));
        user.setUserRole(UserRole.STUDENT);
        AcademicDepartment academicDepartment = academicDepartmentRepository
                .findById(dto.getAcademicDepartmentId())
                .orElseThrow(() -> new RuntimeException("Academic Department not found"));

        user.setAcademicDepartment(academicDepartment);

        User savedUser = repository.save(user);

        return toResponse(savedUser);
    }

    /**
     * Authenticate a user.
     * Steps:
     * - Lookup user by email.
     * - Return "User not found!" if missing.
     * - Verify password using `BCryptPasswordEncoder.matches()`.
     * - Return success or failure messages.
     */
    public UserResponseDto login(UserLoginDto dto) {

        User user = repository.findByUserEmail(dto.getUserEmail());

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        // System.out.println("Password from DB: " + user.getUserPassword());
        // System.out.println("Entered Password: " + dto.getUserPassword());
        if (passwordEncoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            return toResponse(user);
        }

        throw new RuntimeException("Login Unsuccessful!");
    }

    public UserResponseDto getProfileByEmail(String email) {
        User user = repository.findByUserEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found!");
        }

        return toResponse(user);
    }

    private UserResponseDto toResponse(User user) {
        return new UserResponseDto(
                user.getUserId(),
                user.getUserName(),
                user.getUserEmail(),
                user.getUserRole());
    }
}
