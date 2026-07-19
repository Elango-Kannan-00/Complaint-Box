package com.cms.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cms.backend.dto.complaint.HodComplaintResponseDto;
import com.cms.backend.dto.complaint.HodComplaintUpdateDto;
import com.cms.backend.entity.Complaint;
import com.cms.backend.enums.ComplaintStatus;
import com.cms.backend.repository.ComplaintRepository;

@Service
public class HodService {

    @Autowired
    private ComplaintRepository complaintRepository;

    // GET all complaints assigned to HOD
    public List<HodComplaintResponseDto> getComplaintsForHod(Long hodId) {

        List<Complaint> complaints = complaintRepository.findByComplaintDepartmentHodUserId(hodId);

        List<HodComplaintResponseDto> response = new ArrayList<>();

        for (Complaint complaint : complaints) {

            HodComplaintResponseDto dto = new HodComplaintResponseDto();

            dto.setComplaintId(complaint.getComplaintId());
            dto.setComplaintTitle(complaint.getComplaintTitle());
            dto.setComplaintDescription(complaint.getComplaintDescription());
            dto.setStudentName(complaint.getStudent().getUserName());
            dto.setComplaintStatus(complaint.getComplaintStatus());
            dto.setDepartmentName(complaint.getComplaintDepartment().getDepartmentName());
            dto.setCreatedAt(complaint.getCreatedAt());

            response.add(dto);
        }

        return response;
    }

    public String updateComplaintStatus(Long complaintId, HodComplaintUpdateDto dto) {

        Complaint complaint = complaintRepository.findById(complaintId)
                                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        ComplaintStatus currentStatus = complaint.getComplaintStatus();

        ComplaintStatus newStatus = dto.getComplaintStatus();

        if (currentStatus == ComplaintStatus.RESOLVED) {
            throw new RuntimeException("Resolved complaint cannot be modified.");
        }

        if (currentStatus == ComplaintStatus.PENDING && newStatus == ComplaintStatus.RESOLVED) {

            throw new RuntimeException("Complaint must be moved to IN_PROGRESS first.");
        }

        if (currentStatus == newStatus) {
            throw new RuntimeException("Complaint is already in this status.");
        }

        complaint.setComplaintStatus(newStatus);
        complaintRepository.save(complaint);

        return "Complaint status updated successfully.";
    }
}