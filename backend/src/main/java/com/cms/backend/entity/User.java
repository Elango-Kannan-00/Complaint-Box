package com.cms.backend.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.cms.backend.enums.UserRole;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
/**
 * Represents an application user (student, HOD, etc.).
 * Key fields: `userId`, `userName`, `userEmail`, `userPassword`, `userRole`, and `academicDepartment`.
 */
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(name = "academic_department_id")
    private AcademicDepartment academicDepartment;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}