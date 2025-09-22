package com.employee.demo.service;

import com.employee.demo.dto.AttendanceResponseDTO;
import com.employee.demo.dto.AttendenceRequest;
import com.employee.demo.models.Attendance;
import com.employee.demo.models.Users;
import com.employee.demo.respository.AttendenceRepository;
import com.employee.demo.respository.UserDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AttendenceService {

    private final AttendenceRepository attendenceRepository;
    private final UserDetailsRepository usersRepository;

    public AttendenceService(AttendenceRepository attendenceRepository, UserDetailsRepository usersRepository) {
        this.attendenceRepository = attendenceRepository;
        this.usersRepository = usersRepository;
    }
    public ResponseEntity<String> addAttendance() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Users employee = (Users) authentication.getPrincipal();
        LocalDate today = LocalDate.now();

        Attendance attendance = attendenceRepository.findByEmployeeAndDate(employee, today);

        if (attendance != null) {
            // Only update if check-in time is not already set
            if (attendance.getCheckInTime() == null) {
                attendance.setCheckInTime(LocalDateTime.now());
                attendance.setStatus("Present");
                attendance.setRemarks("");
                attendenceRepository.save(attendance);
            }
            System.out.println("Attendance already marked");
        } else {
            // Create new attendance record
            attendance = Attendance.builder()
                    .employee(employee)
                    .date(today)
                    .checkInTime(LocalDateTime.now())
                    .status("Present")
                    .remarks("")
                    .createdAt(LocalDateTime.now())
                    .build();
            attendenceRepository.save(attendance);
            System.out.println("Attendance marked for the first time");
        }

        return ResponseEntity.ok("Attendance created (Check-In successful)");
    }


    public ResponseEntity<String> updateCheckout(Long attendanceId) {
        try {
            // Fetch user
            Users user = usersRepository.findById(attendanceId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Fetch today's attendance
            Attendance attendance = attendenceRepository.findByEmployeeAndDate(user, LocalDate.now());

            if (attendance == null) {
                return ResponseEntity.badRequest().body("No attendance record found for today!");
            }

            // Check if already checked out
//            if (attendance.getCheckOutTime() != null) {
//                return ResponseEntity.badRequest().body("Already checked out!");
//            }

            // Update check-out time
            attendance.setCheckOutTime(LocalDateTime.now());
            attendenceRepository.save(attendance);

            return ResponseEntity.ok("Check-Out successful");

        } catch (Exception e) {
            e.printStackTrace(); // Optional: log the error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    public ResponseEntity<List<AttendanceResponseDTO>> getAllAtendence() {
        try {
            List<Attendance> attendanceList = attendenceRepository.findAll();
            List<AttendanceResponseDTO> dtoList = attendanceList.stream().map(a -> AttendanceResponseDTO.builder()
                    .attendanceId(a.getAttendanceId())
                    .employeeId(a.getEmployee().getId()) // assuming Users entity has getId()
                    .employeeName(a.getEmployee().getUsername()) // assuming Users entity has getName()
                    .date(a.getDate())
                    .checkInTime(a.getCheckInTime())
                    .checkOutTime(a.getCheckOutTime())
                    .status(a.getStatus())
                    .workHours(a.getWorkHours())
                    .remarks(a.getRemarks())
                    .createdAt(a.getCreatedAt())
                    .updatedAt(a.getUpdatedAt())
                    .build()
            ).toList();

            return ResponseEntity.ok(dtoList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    public ResponseEntity<List<Attendance>> getById() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Get the Users object stored in context
            Users user = (Users) authentication.getPrincipal();

            System.out.println("Authenticated user: " + user.getEmail());

            // Fetch attendance by userId
            List<Attendance> attendanceList = attendenceRepository.findByEmployee_Id(user.getId());

            return ResponseEntity.ok(attendanceList);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }



}
