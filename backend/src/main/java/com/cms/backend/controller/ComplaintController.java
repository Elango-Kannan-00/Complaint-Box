package com.cms.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{studentId}")
    public ComplaintResponseDto createComplaint(
            @PathVariable Long studentId,
            @Valid @RequestBody ComplaintRequestDto dto) {

        return service.createComplaint(studentId, dto);
    }

    @GetMapping("/{studentId}")
    public List<ComplaintResponseDto> getAllComplaintsById(@Valid @PathVariable Long id) {
        return service.getComplaintsByStudentId(id);
    }

    @PutMapping("/{complaintId}")
    public ComplaintResponseDto updateComplaint(@PathVariable Long complaintId, @Valid @RequestBody StudentComplaintUpdateDto dto) {
        return service.updateComplaint(complaintId, dto);
    }

    @DeleteMapping("/{complaintId}")
    public String deleteComplaint(@Valid @PathVariable Long complaintId) {
        return service.deleteComplaint(complaintId);
    }
}