package com.cms.backend.dto.complaint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDto {

    @NotBlank(message = "Complaint title is required")
    @Size(max = 100)
    private String complaintTitle;

    @NotBlank(message = "Complaint description is required")
    @Size(max = 1000)
    private String complaintDescription;

    @NotNull(message = "Department is required")
    private Long complaintDepartmentId;
}