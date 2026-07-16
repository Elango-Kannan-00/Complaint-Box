package com.cms.backend.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.repository.AcademicDepartmentRepository;
import com.cms.backend.entity.User;
import com.cms.backend.enums.UserRole;
import com.cms.backend.dto.auth.UserLoginDto;
import com.cms.backend.dto.auth.UserRegistrationDto;
import com.cms.backend.repository.UserRepository;

@Service
public class AuthService {
    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AcademicDepartmentRepository academicDepartmentRepository;

    public String register(UserRegistrationDto dto) {
        if (repository.existsByUserEmail(dto.getUserEmail())) {
            return "User Already Exist";
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

        repository.save(user);

        return "Registration Successful!";
    }

    public String login(UserLoginDto dto) {

        User user = repository.findByUserEmail(dto.getUserEmail());

        if (user == null) {
            return "User not found!";
        }

        if (passwordEncoder.matches(dto.getUserPassword(), user.getUserPassword())) {
            return "Login Successfully!";
        }

        return "Login Unsuccessful!";
    }
}
