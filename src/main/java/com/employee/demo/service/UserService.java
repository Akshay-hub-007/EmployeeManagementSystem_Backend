package com.employee.demo.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.employee.demo.dto.EmployeeDTO;
import com.employee.demo.dto.UpdateUserRequest;
import com.employee.demo.dto.UserRequest;
import com.employee.demo.models.Attendance;
import com.employee.demo.models.Users;
import com.employee.demo.respository.AttendenceRepository;
import com.employee.demo.respository.UserDetailsRepository;
import com.employee.demo.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @Autowired
    JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AttendenceRepository attendenceRepository;
    @Autowired
    AuthenticationManager authenticationManager;

    public String register(Users u) {
        try {
            System.out.println(u);
            u.setPassword(passwordEncoder.encode(u.getPassword()));
            userDetailsRepository.save(u);
            return "User saved successfully";
        } catch (Exception e) {
            // Log the exception if needed
            e.printStackTrace();
            return "Failed to save user: " + e.getMessage();
        }
    }


    public String sign(UserRequest userRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userRequest.getEmail(),
                            userRequest.getPassword()
                    )
            );
            Users user = userDetailsRepository.findByEmail(userRequest.getEmail()).orElseThrow(() ->
                    new UsernameNotFoundException("user not found"));
            String role = String.valueOf(user.getRole());
            return jwtUtil.generateToken(userRequest.getEmail(), role);
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password", e);
        }
    }

    public Users updateDetails(UpdateUserRequest updatedData, Long id) {
        try {
            Users user = userDetailsRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));
            System.out.println(updatedData.getPhoneNumber());
            if (updatedData.getUsername() != null) user.setUsername(updatedData.getUsername());
            if (updatedData.getPhoneNumber() != null) user.setPhoneNumber(updatedData.getPhoneNumber());
            if (updatedData.getAddress() != null) user.setAddress(updatedData.getAddress());
            if (updatedData.getDepartment() != null) user.setDepartment(updatedData.getDepartment());
            if (updatedData.getPosition() != null) user.setPosition(updatedData.getPosition());
            if (updatedData.getJoinedAt() != null) user.setJoinedAt(updatedData.getJoinedAt());
            if (updatedData.getDateOfBirth() != null) user.setDateOfBirth(updatedData.getDateOfBirth());

            Users user1 = userDetailsRepository.save(user);

            System.out.println(user1.getDepartment());
            return user1;

        } catch (NoSuchElementException e) {
            System.err.println("User not found: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw new RuntimeException("Failed to update user", e);
        }
    }

    public List<EmployeeDTO> getAllEmployees() {
        List<Users> users = userDetailsRepository.findAll();

        return users.stream()
                .map(user -> {
                    // Fetch attendance for today
                    Attendance attendance = attendenceRepository.findByEmployeeAndDate(user, LocalDate.now());

                    String status = (attendance != null) ? attendance.getStatus() : "ABSENT";

                    return new EmployeeDTO(
                            user.getId(),
                            user.getUsername(),
                            user.getDepartment(),
                            user.getPosition(),
                            user.getSalary(),
                            status,
                            user.getJoinedAt(),
                            user.getEmail(),
                            user.getRole()
                    );
                })
                .collect(Collectors.toList());
    }

}