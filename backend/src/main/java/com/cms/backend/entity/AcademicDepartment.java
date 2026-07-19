package com.cms.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "academic_departments")
/**
 * Represents an academic department (e.g., Computer Science). Used to group students and
 * to link academic complaint departments.
 */
public class AcademicDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_department_id")
    private Long academicDepartmentId;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;
}