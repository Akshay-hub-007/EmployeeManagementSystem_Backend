package com.employee.demo.controller;

import com.employee.demo.dto.EmployeeDTO;
import com.employee.demo.dto.UpdateUserRequest;
import com.employee.demo.dto.UserRequest;
import com.employee.demo.dto.UserResponse;
import com.employee.demo.models.ROLE;
import com.employee.demo.models.Users;
import com.employee.demo.respository.UserDetailsRepository;
import com.employee.demo.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
public class UserController {

    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    private UserService userService;
    @PostMapping("/register")
    public String register(@RequestBody Users u)
    {
        return  userService.register(u);
    }

    @Value("${app.cookie.secure}")
    private boolean cookieSecure;

    @Value("${app.cookie.samesite}")
    private String cookieSameSite;

    @PostMapping("/sign")
    public ResponseEntity<UserResponse> signin(@RequestBody UserRequest userRequest, HttpServletResponse response) {
        System.out.println("sign..");
        Users user = userDetailsRepository.findByEmail(userRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));

        String token = userService.sign(userRequest);

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);  // comes from properties
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setAttribute("SameSite", cookieSameSite); // comes from properties
        response.addCookie(cookie);

        return ResponseEntity.ok(new UserResponse("Login successful", user));
    }
    @PostMapping("/api/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(cookieSecure);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setAttribute("SameSite", cookieSameSite);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }

    @GetMapping("/authenticated")
    public ResponseEntity<String> authenticated()
    {
        System.out.println("auth");
        return  ResponseEntity.ok("Authenticated");

    }

    @PutMapping("/updateDetails/{id}")
    public ResponseEntity<?> updateDetails(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest updatedData
    ) {
        try {

            System.out.println("Uodating deyak");
            Users updatedUser = userService.updateDetails(updatedData, id);
            System.out.println(updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user");
        }
    }

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        try {
            System.out.println("Fetching employee list");
            List<EmployeeDTO> employees = userService.getAllEmployees();

            // Better logging for readability
//            employees.forEach(e -> System.out.println(e.getUsername() + " - " + e.getAttendanceStatus()));

            return ResponseEntity.ok(employees);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/toggleRole/{id}")
    public ResponseEntity<?> toggleUserRole(@PathVariable Long id) {
        try {
            System.out.println(id);
            Users user = userDetailsRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("User not found"));

            // Toggle role
            if (user.getRole() == ROLE.ADMIN) {
                user.setRole(ROLE.EMPLOYEE);
            } else {
                user.setRole(ROLE.ADMIN);
            }

            userDetailsRepository.save(user);

            return ResponseEntity.ok("User role updated to " + user.getRole());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user role");
        }
    }


}
