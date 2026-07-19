package com.cms.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.cms.backend.dto.complaint.ComplaintFeedbackDto;
import com.cms.backend.dto.complaint.ComplaintRequestDto;
import com.cms.backend.dto.complaint.ComplaintResponseDto;
import com.cms.backend.dto.complaint.StudentComplaintUpdateDto;
import com.cms.backend.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService service;

    /**
     * Create complaint for a student. Delegates to `ComplaintService.createComplaint`.
     */
    @PostMapping("/{studentId}")
    public ComplaintResponseDto createComplaint(@PathVariable Long studentId, @Valid @RequestBody ComplaintRequestDto dto) {
        return service.createComplaint(studentId, dto);
    }

    /**
     * Get all complaints for a student id.
     */
    @GetMapping("/{studentId}")
    public List<ComplaintResponseDto> getAllComplaintsById(@Valid @PathVariable Long studentId) {
        return service.getComplaintsByStudentId(studentId);
    }

    /**
     * Update a complaint's title and description.
     */
    @PutMapping("/{complaintId}")
    public ComplaintResponseDto updateComplaint(@PathVariable Long complaintId, @Valid @RequestBody StudentComplaintUpdateDto dto) {
        return service.updateComplaint(complaintId, dto);
    }

    /**
     * Delete a complaint if allowed by business rules.
     */
    @DeleteMapping("/{complaintId}")
    public String deleteComplaint(@Valid @PathVariable Long complaintId) {
        return service.deleteComplaint(complaintId);
    }

    /**
     * Submit feedback for a resolved complaint.
     */
    @PutMapping("/{complaintId}/feedback")
    public String submitFeedback(@PathVariable Long complaintId,@Valid @RequestBody ComplaintFeedbackDto dto) {

        return service.submitFeedback(complaintId, dto);
    }
}