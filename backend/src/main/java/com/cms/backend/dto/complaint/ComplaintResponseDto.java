package com.cms.backend.dto.complaint;

import java.sql.Timestamp;

import com.cms.backend.enums.ComplaintStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintResponseDto {

    private Long complaintId;

    private String complaintTitle;

    private String complaintDescription;

    private ComplaintStatus complaintStatus;

    private String studentName;

    private String departmentName;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    private String feedback;
}