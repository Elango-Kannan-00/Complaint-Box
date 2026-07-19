package com.cms.backend.dto.complaint;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintFeedbackDto {

    /**
     * DTO carrying feedback text submitted by a student after complaint is resolved.
     */

    @NotBlank(message = "Feedback is required")
    @Size(max = 500, message = "Feedback cannot exceed 500 characters")
    private String feedback;
}