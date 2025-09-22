package com.employee.demo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnore
    private Users employee;

    @NotNull
    private LocalDate date = LocalDate.now();

    private LocalDateTime checkInTime = LocalDateTime.now();

    private LocalDateTime checkOutTime;

    @Column(length = 20)
    private String status;  // Present, Absent, Half-day, Leave

    private Double workHours;  // Auto-calculated

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Additional fields
    @Column(length = 50)
    private String department;

    private LocalDate joinedAt;

    @Column(length = 15)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(length = 50)
    private String position;

    @PrePersist
    @PreUpdate
    protected void calculateFields() {
        this.updatedAt = LocalDateTime.now();

        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }

        // Set joinedAt only once when the row is created
        if (this.joinedAt == null) {
            this.joinedAt = LocalDate.now();
        }

        // Auto calculate workHours if check-in & check-out are present
        if (checkInTime != null && checkOutTime != null) {
            Duration duration = Duration.between(checkInTime, checkOutTime);
            this.workHours = duration.toMinutes() / 60.0; // convert to hours
        }
    }
}
