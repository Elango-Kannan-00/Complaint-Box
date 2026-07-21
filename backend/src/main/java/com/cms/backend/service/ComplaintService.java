package com.cms.backend.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.backend.dto.complaint.ComplaintFeedbackDto;
import com.cms.backend.dto.complaint.ComplaintRequestDto;
import com.cms.backend.dto.complaint.ComplaintResponseDto;
import com.cms.backend.dto.complaint.StudentComplaintUpdateDto;
import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.entity.Complaint;
import com.cms.backend.entity.ComplaintDepartment;
import com.cms.backend.entity.User;
import com.cms.backend.enums.ComplaintStatus;
import com.cms.backend.enums.DepartmentType;
import com.cms.backend.repository.ComplaintDepartmentRepository;
import com.cms.backend.repository.ComplaintRepository;
import com.cms.backend.repository.UserRepository;

@Service
public class ComplaintService {

    /**
     * Service that implements business logic for creating, reading, updating,
     * deleting complaints and submitting feedback. Methods enforce rules such
     * as time-window edits (1 hour) and allowed status transitions.
     */

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private ComplaintDepartmentRepository complaintDepartmentRepository;

    @Autowired
    private UserRepository userRepository;

        /**
         * Create a new complaint submitted by `studentId`.
         * - Validates student and complaint department.
         * - Enforces: student may complain only to common departments or their own academic department.
         * - Sets status to PENDING and persists the complaint.
         */
        public ComplaintResponseDto createComplaint(Long studentId, ComplaintRequestDto dto) {

        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ComplaintDepartment complaintDepartment = complaintDepartmentRepository.findById(dto.getComplaintDepartmentId())
                .orElseThrow(() -> new RuntimeException("Complaint Department not found"));

        /*
         * Rule:
         * Student can complain only to
         * 1. Common Departments
         * 2. Their own Academic Department
         */

        if (complaintDepartment.getDepartmentType() == DepartmentType.ACADEMIC) {

            AcademicDepartment studentDepartment = student.getAcademicDepartment();

            AcademicDepartment complaintAcademicDepartment = complaintDepartment.getAcademicDepartment();

            if (!studentDepartment.getAcademicDepartmentId()
                    .equals(complaintAcademicDepartment.getAcademicDepartmentId())) {

                throw new RuntimeException(
                        "You cannot raise complaint to another academic department.");
            }
        }

        Complaint complaint = new Complaint();

        complaint.setComplaintTitle(dto.getComplaintTitle());
        complaint.setComplaintDescription(dto.getComplaintDescription());
        complaint.setComplaintStatus(ComplaintStatus.PENDING);
        complaint.setStudent(student);
        complaint.setComplaintDepartment(complaintDepartment);

        Complaint savedComplaint = complaintRepository.save(complaint);

        ComplaintResponseDto response = new ComplaintResponseDto();

        response.setComplaintId(savedComplaint.getComplaintId());
        response.setComplaintTitle(savedComplaint.getComplaintTitle());
        response.setComplaintDescription(savedComplaint.getComplaintDescription());
        response.setComplaintStatus(savedComplaint.getComplaintStatus());
        response.setDepartmentName(savedComplaint.getComplaintDepartment().getDepartmentName());
        response.setCreatedAt(savedComplaint.getCreatedAt());
        response.setUpdatedAt(savedComplaint.getUpdatedAt());
        response.setFeedback(savedComplaint.getFeedback());

        return response;
    }

    /**
     * Retrieve all complaints for the given student id and map to response DTOs.
     */
    public List<ComplaintResponseDto> getComplaintsByStudentId(Long id) {
        List<ComplaintResponseDto> response = new ArrayList<>();

        List<Complaint> allComplaints = complaintRepository.findByStudentUserId(id);

        for (Complaint complaint : allComplaints) {
            ComplaintResponseDto dto = new ComplaintResponseDto();

            dto.setComplaintId(complaint.getComplaintId());
            dto.setComplaintTitle(complaint.getComplaintTitle());
            dto.setComplaintDescription(complaint.getComplaintDescription());
            dto.setComplaintStatus(complaint.getComplaintStatus());
            dto.setDepartmentName(complaint.getComplaintDepartment().getDepartmentName());
            dto.setCreatedAt(complaint.getCreatedAt());
            dto.setUpdatedAt(complaint.getUpdatedAt());
            dto.setFeedback(complaint.getFeedback());

            response.add(dto);
        }

        return response;
    }

    /**
     * Update a complaint's title/description.
     * - Allows edits only within 1 hour of creation and only when status is PENDING.
     */
    public ComplaintResponseDto updateComplaint(Long complaintId, StudentComplaintUpdateDto dto) {
        Complaint complaint = complaintRepository
                .findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        if (complaint.getComplaintId() != complaintId) {
            throw new RuntimeException("Complaint Not exists");
        }

        Timestamp created = complaint.getCreatedAt();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        long difference = now.getTime() - created.getTime();

        if (difference > 3600000) {
            throw new RuntimeException("Complaint can only be edited within 1 hour.");
        }

        if (complaint.getComplaintStatus() != ComplaintStatus.PENDING) {
            throw new RuntimeException("Complaint cannot be edited after processing.");
        }

        complaint.setComplaintTitle(dto.getComplaintTitle());
        complaint.setComplaintDescription(dto.getComplaintDescription());

        Complaint updatedComplaint = complaintRepository.save(complaint);

        ComplaintResponseDto response = new ComplaintResponseDto();
        response.setComplaintTitle(updatedComplaint.getComplaintTitle());
        response.setComplaintDescription(updatedComplaint.getComplaintDescription());

        return response;
    }

    /**
     * Delete a complaint if within 1 hour and still PENDING.
     */
    public String deleteComplaint(Long complaintId) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint Not Found"));

        Timestamp created = complaint.getCreatedAt();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        long difference = now.getTime() - created.getTime();

        if (difference > 3600000) {
            throw new RuntimeException("Complaint can only be deleted within 1 hour.");
        }

        if (complaint.getComplaintStatus() != ComplaintStatus.PENDING) {
            throw new RuntimeException("Complaint cannot be deleted after processing.");
        }

        complaintRepository.delete(complaint);

        return "Complaint Deleted Successfully";
    }

    /**
     * Submit feedback for a resolved complaint.
     * - Requires complaint status RESOLVED and prevents duplicate feedback.
     */
    public String submitFeedback(Long complaintId, ComplaintFeedbackDto dto) {

        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        // Only resolved complaints can receive feedback
        if (complaint.getComplaintStatus() != ComplaintStatus.RESOLVED) {
            throw new RuntimeException("Feedback can only be submitted after the complaint is resolved.");
        }

        // Prevent duplicate feedback
        if (complaint.getFeedback() != null && !complaint.getFeedback().isBlank()) {
            throw new RuntimeException("Feedback has already been submitted.");
        }

        complaint.setFeedback(dto.getFeedback());
        complaintRepository.save(complaint);

        return "Feedback submitted successfully.";
    }
}