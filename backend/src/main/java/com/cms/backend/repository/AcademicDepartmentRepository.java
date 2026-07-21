package com.cms.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.backend.entity.AcademicDepartment;

/**
 * Repository for `AcademicDepartment` entities.
 */
public interface AcademicDepartmentRepository extends JpaRepository<AcademicDepartment, Long> {

    Optional<AcademicDepartment> findByDepartmentName(String departmentName);
}
