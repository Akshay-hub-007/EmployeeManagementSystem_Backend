package com.employee.demo.controller;

import com.employee.demo.dto.LeaveRequestDTO;
import com.employee.demo.dto.LeaveResponseDTO;
import com.employee.demo.dto.StatusUpdateRequest;
import com.employee.demo.models.Leave;
import com.employee.demo.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    LeaveService leaveService;
    @PostMapping("/apply")
    public ResponseEntity<String> applyRequest(@RequestBody LeaveRequestDTO leaveRequestDTO) {
        try {
            String message = leaveService.applyLeave(leaveRequestDTO);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while applying for leave.");
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<List<Leave>> getAllLeaves() {
        try {
            List<Leave> leaves = leaveService.getAllLeaves();
            return ResponseEntity.ok(leaves);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList());
        }
    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<String> actions(@PathVariable Long id, @RequestBody StatusUpdateRequest statusUpdateRequest) {
        try {
            System.out.println("getting all requests");
            String message = leaveService.actions(id, statusUpdateRequest.getStatus());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update leave status: " + e.getMessage());
        }
    }

    @GetMapping("/getleaves/{id}")
    public ResponseEntity<List<Leave>> listResponseEntity(@PathVariable Long id) {
        List<Leave> leaves = leaveService.getAllLeavesById(id);
        return ResponseEntity.ok(leaves);
    }


}
