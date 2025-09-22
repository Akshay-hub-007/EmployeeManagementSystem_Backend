package com.employee.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "leaves")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Leave type: e.g. Annual, Sick, Casual, etc.
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false, length = 500)
    private String reason;

    private LocalDate appliedAt = LocalDate.now();

    private String emergencyContact;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Users employee;

    @Column(nullable = false)
    private String status = "Pending";

    private LocalDate createdAt = LocalDate.now();

    @Transient
    @JsonProperty("employeeName")
    public String getEmployeeName() {
        return employee != null ? employee.getUsername() : null;
    }
}
