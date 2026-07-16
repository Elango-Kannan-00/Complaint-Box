package com.cms.backend.dto.department;

import com.cms.backend.enums.DepartmentType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintDepartmentDto {
    private Long complaintDepartmentId;
    private String complaintDepartmentName;
    private DepartmentType departmentType;
}
