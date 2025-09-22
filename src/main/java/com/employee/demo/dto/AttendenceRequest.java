package com.employee.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AttendenceRequest {
    private Long employeeId;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime; // will be null at check-in, set later at check-out
    private String status;
    private String remarks;
}
