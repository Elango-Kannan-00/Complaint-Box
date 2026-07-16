package com.cms.backend.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.cms.backend.enums.ComplaintStatus;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "complaints")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "complaint_id")
    private Long complaintId;

    @Column(name = "complaint_title", nullable = false)
    private String complaintTitle;

    @Column(name = "complaint_description", length = 1000, nullable = false)
    private String complaintDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "complaint_status")
    private ComplaintStatus complaintStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "complaint_department_id")
    private ComplaintDepartment complaintDepartment;

    @Column(length = 500)
    private String feedback;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
}