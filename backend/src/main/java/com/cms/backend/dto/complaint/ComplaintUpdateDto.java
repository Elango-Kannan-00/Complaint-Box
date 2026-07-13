package com.cms.backend.dto.complaint;

import com.cms.backend.enums.ComplaintStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintUpdateDto {

    @NotNull(message = "Complaint status is required")
    private ComplaintStatus complaintStatus;
}