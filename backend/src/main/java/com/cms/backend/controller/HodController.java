package com.cms.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.backend.dto.complaint.HodComplaintResponseDto;
import com.cms.backend.dto.complaint.HodComplaintUpdateDto;
import com.cms.backend.service.HodService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/hod")
public class HodController {
    @Autowired
    private HodService service;

    @GetMapping("/{hodId}/complaints")
    public List<HodComplaintResponseDto> getComplaints(@PathVariable Long hodId) {
        return service.getComplaintsForHod(hodId);
    }

    @PutMapping("/complaints/{complaintId}/status")
    public String updateComplaintStatus(@PathVariable Long complaintId,@Valid @RequestBody HodComplaintUpdateDto dto) {
        return service.updateComplaintStatus(complaintId, dto);
    }
}
