package com.employee.demo.controller;

import com.employee.demo.dto.AttendanceResponseDTO;
import com.employee.demo.dto.AttendenceRequest;
import com.employee.demo.models.Attendance;
import com.employee.demo.service.AttendenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/attendance")
public class AttendenceController {

    private final AttendenceService attendenceService;

    public AttendenceController(AttendenceService attendenceService) {
        this.attendenceService = attendenceService;
    }

    @PostMapping("/checkin")
    public ResponseEntity<String> createAttendence() {

        return attendenceService.addAttendance();
    }

    @PutMapping("/{id}/checkout")
    public ResponseEntity<String> checkout(@PathVariable Long id) {
        System.out.println(id);
        return attendenceService.updateCheckout(id);
    }

    @GetMapping("/getAttendence")
    public ResponseEntity<List<AttendanceResponseDTO>> getAllAttendence() {
        try {
            return attendenceService.getAllAtendence();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

    @GetMapping("/getAttendenceById")
    public ResponseEntity<List<Attendance>> getById() {
        try {
            return attendenceService.getById();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }

}
