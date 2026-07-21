package com.cms.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.entity.ComplaintDepartment;
import com.cms.backend.enums.DepartmentType;

/**
 * Repository for `ComplaintDepartment` entities.
 * Provides lookups by department type and by associated academic department.
 */
public interface ComplaintDepartmentRepository extends JpaRepository<ComplaintDepartment, Long> {

    List<ComplaintDepartment> findByDepartmentType(DepartmentType departmentType);

    Optional<ComplaintDepartment> findByAcademicDepartment(AcademicDepartment academicDepartment);

    Optional<ComplaintDepartment> findByDepartmentName(String departmentName);

}
