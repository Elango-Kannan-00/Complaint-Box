package com.cms.backend.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AcademicDepartmentDto {
    /**
     * DTO representing an academic department for API responses.
     */
    private Long academicDepartmentId;
    private String academicDepartmentName;
}
