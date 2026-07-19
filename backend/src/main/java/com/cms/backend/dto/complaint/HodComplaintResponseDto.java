package com.cms.backend.dto.complaint;

import java.sql.Timestamp;

import com.cms.backend.enums.ComplaintStatus;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HodComplaintResponseDto {

    /**
     * DTO returned to HODs listing complaints assigned to them.
     */

    private Long complaintId;

    private String complaintTitle;

    private String complaintDescription;

    private String studentName;

    private ComplaintStatus complaintStatus;

    private String departmentName;

    private Timestamp createdAt;
}