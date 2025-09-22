package com.employee.demo.service;


import com.employee.demo.models.Users;
import com.employee.demo.respository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserDetailsRepository userDetailsRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        System.out.println("Received username: " + email);

        Users user = userDetailsRepository.findByEmail(email.trim())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        System.out.println("User found: " + user + " hello");
        return user;
    }
}
