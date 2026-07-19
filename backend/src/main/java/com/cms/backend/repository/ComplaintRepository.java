package com.cms.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.backend.entity.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByStudentUserId(Long userId);
    List<Complaint> findByComplaintDepartmentHodUserId(Long hodId);
}
