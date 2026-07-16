package com.cms.backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "academic_departments")
public class AcademicDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "academic_department_id")
    private Long academicDepartmentId;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;
}