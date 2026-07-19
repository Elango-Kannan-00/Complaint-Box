package com.cms.backend.dto.department;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponseDto {

    /**
     * Generic department DTO used where only id/name are required.
     */

    private Long departmentId;

    private String departmentName;
}