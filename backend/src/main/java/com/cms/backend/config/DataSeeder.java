package com.cms.backend.config;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cms.backend.entity.AcademicDepartment;
import com.cms.backend.entity.ComplaintDepartment;
import com.cms.backend.enums.DepartmentType;
import com.cms.backend.repository.AcademicDepartmentRepository;
import com.cms.backend.repository.ComplaintDepartmentRepository;

@Configuration
public class DataSeeder {

    private static final List<String> ACADEMIC_DEPARTMENTS = List.of(
            "CSE",
            "ECE",
            "IT",
            "AIDS",
            "CSBS",
            "EEE",
            "MECH",
            "CIVIL",
            "ECX");

    private static final List<String> COMMON_COMPLAINT_DEPARTMENTS = List.of(
            "Hostel",
            "Infrastructure",
            "Library",
            "Transport",
            "Physical Education");

    @Bean
    CommandLineRunner seedDepartments(
            AcademicDepartmentRepository academicDepartmentRepository,
            ComplaintDepartmentRepository complaintDepartmentRepository) {
        return args -> {
            seedAcademicDepartments(academicDepartmentRepository);
            seedComplaintDepartments(academicDepartmentRepository, complaintDepartmentRepository);
        };
    }

    private void seedAcademicDepartments(AcademicDepartmentRepository repository) {
        for (String name : ACADEMIC_DEPARTMENTS) {
            repository.findByDepartmentName(name)
                    .orElseGet(() -> repository.save(department(name)));
        }
    }

    private void seedComplaintDepartments(
            AcademicDepartmentRepository academicDepartmentRepository,
            ComplaintDepartmentRepository complaintDepartmentRepository) {
        Map<String, AcademicDepartment> academicDepartments = academicDepartmentRepository.findAll().stream()
                .collect(Collectors.toMap(AcademicDepartment::getDepartmentName, Function.identity()));

        List<ComplaintDepartment> departments = COMMON_COMPLAINT_DEPARTMENTS.stream()
                .map(name -> upsertComplaintDepartment(complaintDepartmentRepository, name, DepartmentType.COMMON, null))
                .toList();

        List<ComplaintDepartment> academicComplaintDepartments = ACADEMIC_DEPARTMENTS.stream()
                .map(name -> upsertComplaintDepartment(
                        complaintDepartmentRepository,
                        name,
                        DepartmentType.ACADEMIC,
                        academicDepartments.get(name)))
                .toList();

        complaintDepartmentRepository.saveAll(departments);
        complaintDepartmentRepository.saveAll(academicComplaintDepartments);
    }

    private AcademicDepartment department(String name) {
        AcademicDepartment department = new AcademicDepartment();
        department.setDepartmentName(name);
        return department;
    }

    private ComplaintDepartment complaintDepartment(
            String name,
            DepartmentType type,
            AcademicDepartment academicDepartment) {
        ComplaintDepartment department = new ComplaintDepartment();
        department.setDepartmentName(name);
        department.setDepartmentType(type);
        department.setAcademicDepartment(academicDepartment);
        return department;
    }

    private ComplaintDepartment upsertComplaintDepartment(
            ComplaintDepartmentRepository repository,
            String name,
            DepartmentType type,
            AcademicDepartment academicDepartment) {
        ComplaintDepartment department = repository.findByDepartmentName(name)
                .orElseGet(ComplaintDepartment::new);
        department.setDepartmentName(name);
        department.setDepartmentType(type);
        department.setAcademicDepartment(academicDepartment);
        return department;
    }
}
