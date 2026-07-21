package com.cms.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.backend.dto.department.AcademicDepartmentDto;
import com.cms.backend.dto.department.ComplaintDepartmentDto;
import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.entity.ComplaintDepartment;
import com.cms.backend.entity.User;
import com.cms.backend.enums.DepartmentType;
import com.cms.backend.repository.AcademicDepartmentRepository;
import com.cms.backend.repository.ComplaintDepartmentRepository;
import com.cms.backend.repository.UserRepository;

/**
 * Service that returns academic and complaint department DTOs used by controllers.
 * - `getAllAcademicDepartments` returns all academic departments mapped to DTOs.
 * - `getComplaintDepartments` returns common complaint departments plus the student's academic complaint department.
 */
@Service
public class DepartmentService {
    @Autowired
    private AcademicDepartmentRepository academicDepartmentRepository;

    @Autowired
    private ComplaintDepartmentRepository complaintDepartmentRepository;

    @Autowired
    private UserRepository userRepository;

        /**
         * Retrieve all academic departments and map to `AcademicDepartmentDto`.
         */
        public List<AcademicDepartmentDto> getAllAcademicDepartments() {
        List<AcademicDepartment> academicDepartments = academicDepartmentRepository.findAll();

        List<AcademicDepartmentDto> response = new ArrayList<>();

        for (AcademicDepartment department : academicDepartments) {
            AcademicDepartmentDto obj = new AcademicDepartmentDto();

            obj.setAcademicDepartmentId(department.getAcademicDepartmentId());
            obj.setAcademicDepartmentName(department.getDepartmentName());

            response.add(obj);
        }

        return response;
    }

        /**
         * Return complaint departments available to a student:
         * - all departments with type COMMON
         * - the academic complaint department for the student's academic department
         */
        public List<ComplaintDepartmentDto> getComplaintDepartments(Long studentId) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        AcademicDepartment academicDepartment = student.getAcademicDepartment();

        List<ComplaintDepartment> departments = complaintDepartmentRepository.findByDepartmentType(
                DepartmentType.COMMON);

        Optional<ComplaintDepartment> academicComplaintDepartment = complaintDepartmentRepository
                .findByAcademicDepartment(academicDepartment)
                .or(() -> complaintDepartmentRepository.findByDepartmentName(academicDepartment.getDepartmentName()));

        academicComplaintDepartment.ifPresent(departments::add);

        List<ComplaintDepartmentDto> response = new ArrayList<>();

        for (ComplaintDepartment department : departments) {

            ComplaintDepartmentDto dto = new ComplaintDepartmentDto();

            dto.setComplaintDepartmentId(
                    department.getComplaintDepartmentId());

            dto.setComplaintDepartmentName(
                    department.getDepartmentName());

            dto.setDepartmentType(
                    department.getDepartmentType());

            response.add(dto);
        }

        return response;
    }
}
