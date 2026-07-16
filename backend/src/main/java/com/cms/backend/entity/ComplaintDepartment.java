package com.cms.backend.entity;

import com.cms.backend.enums.DepartmentType;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaint_departments")
public class ComplaintDepartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_department_id")
    private Long complaintDepartmentId;

    @Column(name = "department_name", nullable = false, unique = true)
    private String departmentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "department_type", nullable = false)
    private DepartmentType departmentType;

    @OneToOne
    @JoinColumn(name = "hod_user_id")
    private User hod;

    @ManyToOne
    @JoinColumn(name = "academic_department_id")
    private AcademicDepartment academicDepartment;
}