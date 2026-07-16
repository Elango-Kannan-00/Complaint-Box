package com.cms.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.backend.dto.department.AcademicDepartmentDto;
import com.cms.backend.dto.department.ComplaintDepartmentDto;
import com.cms.backend.service.DepartmentService;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService service;

    @GetMapping("/academic-departments")
    public List<AcademicDepartmentDto> getAllAcademicDepartments() {
        return service.getAllAcademicDepartments();
    }

    @GetMapping("/complaint-departments/{studentId}")
    public List<ComplaintDepartmentDto> getAllCompaintDepartments(@PathVariable Long studentId) {
        return service.getComplaintDepartments(studentId);
    }
}
