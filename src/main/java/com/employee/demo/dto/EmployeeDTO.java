package com.employee.demo.dto;

import com.employee.demo.models.ROLE;

import java.time.LocalDate;

public class EmployeeDTO {

    private Long id;            // optional, for internal use
    private String employee;    // employee name
    private String department;
    private String position;
    private Double salary;
    private String status;      // e.g., ACTIVE, INACTIVE
    private LocalDate joinDate;
    private String email;
    private ROLE role;
    public EmployeeDTO() {}


    public EmployeeDTO(Long id, String employee, String department, String position,
                       Double salary, String status, LocalDate joinDate,String email, ROLE  role) {
        this.id=id;
        this.employee = employee;
        this.department = department;
        this.position = position;
        this.salary = salary;
        this.status = status;
        this.joinDate = joinDate;
        this.email=email;
        this.role=role;
    }



    public ROLE getRole() {
        return role;
    }

    public void setRole(ROLE role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joinDate = joinDate;
    }
}
