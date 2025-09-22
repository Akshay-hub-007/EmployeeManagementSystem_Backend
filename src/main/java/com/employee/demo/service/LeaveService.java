package com.employee.demo.service;

import com.employee.demo.dto.EmployeeDTO;
import com.employee.demo.dto.LeaveRequestDTO;
import com.employee.demo.dto.LeaveResponseDTO;
import com.employee.demo.models.Attendance;
import com.employee.demo.models.Leave;
import com.employee.demo.models.Users;
import com.employee.demo.respository.AttendenceRepository;
import com.employee.demo.respository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LeaveService {


    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    AttendenceRepository attendenceRepository;
    public String applyLeave(LeaveRequestDTO dto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Users employee = (Users) authentication.getPrincipal();

            Leave leave = Leave.builder()
                    .type(dto.getType())
                    .startDate(dto.getStartDate())
                    .endDate(dto.getEndDate())
                    .reason(dto.getReason())
                    .emergencyContact(dto.getEmergencyContact())
                    .employee(employee)
                    .status("Pending")
                    .createdAt(LocalDate.now())
                    .build();

            leaveRepository.save(leave);
            return "Leave application submitted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to apply leave.";
        }
    }



    public List<Leave> getAllLeaves() {
        try {
            System.out.println("getting all requests");
            return leaveRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching leaves", e);
        }
    }
    public String actions(Long id, String status) {
        try {
            Leave leave = leaveRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Leave not found with id: " + id));

            Users employee = leave.getEmployee();

            // Update leave status
            leave.setStatus(status);
            leaveRepository.save(leave);

            // If leave is approved, mark attendance as Absent for leave period
            if ("Approved".equalsIgnoreCase(status)) {
                LocalDate start = leave.getStartDate();
                LocalDate end = leave.getEndDate();

                for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
                    LocalDate finalDate = date;
                    Attendance attendance = attendenceRepository.findByEmployeeAndDate(employee, date);

                    if (attendance == null) {
                        attendance = Attendance.builder()
                                .employee(employee)
                                .date(date)
                                .status("Absent")
                                .remarks("Leave approved")
                                .department(employee.getDepartment())
                                .position(employee.getPosition())
                                .build();
                    } else {
                        attendance.setStatus("Absent");
                        attendance.setRemarks("Leave approved");
                    }
                    attendance.setStatus("Absent");
                    attendance.setRemarks("Leave approved");
                    attendenceRepository.save(attendance);
                }
            }

            return String.format("Leave for %s is %s", leave.getEmployeeName(), status);
        } catch (Exception e) {
            throw new RuntimeException("Error updating leave status", e);
        }
    }

    public List<Leave> getAllLeavesById(Long employeeId)
    {
        System.out.println("getting all ");
        return leaveRepository.findAllByEmployeeId(employeeId);
    }

}
