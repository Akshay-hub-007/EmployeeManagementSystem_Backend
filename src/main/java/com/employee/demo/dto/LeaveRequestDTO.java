package com.employee.demo.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestDTO {


    private String type;            // Annual, Sick, etc.
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String emergencyContact; // optional
    private String status;           // Pending, Approved, Rejected

    // For UI display only (not needed in request)
    private String employeeName;
}
