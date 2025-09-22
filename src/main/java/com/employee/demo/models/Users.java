    package com.employee.demo.models;

    import jakarta.persistence.*;
    import jakarta.validation.constraints.NotNull;
    import lombok.Builder;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.authority.SimpleGrantedAuthority;
    import org.springframework.security.core.userdetails.UserDetails;

    import java.time.LocalDate;
    import java.util.Collection;
    import java.util.List;

    @Entity
    public class Users implements UserDetails {

        public Users() {}

        public Users(String name, ROLE role, String password, String email) {
            this.username = name;
            this.role = role;
            this.password = password;
            this.email = email;
        }

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        private Long id;

        @NotNull
        private String username;

        @Enumerated(EnumType.STRING)
        private ROLE role = ROLE.EMPLOYEE;

        @NotNull
        private String password;

        @Column(unique = true)
        private String email;

        // New employee-specific fields
        @Column(length = 50)
        private String department;

        private LocalDate joinedAt = LocalDate.now();

        @Column(length = 15)
        private String phoneNumber;

        @Column(length = 255)
        private String address;
        @Column
        private LocalDate dateOfBirth;


        @Column(length = 50)
        private String position;

        private Double salary;

        public Double getSalary() {
            return salary;
        }

        public void setSalary(Double salary) {
            this.salary = salary;
        }
        // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        public void setUsername(String name) {
            this.username = name;
        }

        public ROLE getRole() {
            return role;
        }

        public void setRole(ROLE role) {
            this.role = role;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
        }

        @Override
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
        // Getter and Setter
        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(LocalDate dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        // Employee fields getters and setters
        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public LocalDate getJoinedAt() {
            return joinedAt;
        }

        public void setJoinedAt(LocalDate joinedAt) {
            this.joinedAt = joinedAt;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }
    }
