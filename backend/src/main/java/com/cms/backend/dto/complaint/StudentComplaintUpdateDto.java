package com.cms.backend.dto.complaint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentComplaintUpdateDto {
    @NotBlank
    @Size(max = 100)
    private String complaintTitle;

    @NotBlank
    @Size(max = 1000)
    private String complaintDescription;
}
