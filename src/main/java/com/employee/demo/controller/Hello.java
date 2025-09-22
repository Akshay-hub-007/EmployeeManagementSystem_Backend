package com.employee.demo.controller;

import com.employee.demo.respository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {

        @Autowired
        UserDetailsRepository userDetailsRepository;
        @GetMapping("/hello")
        public String hello() {
                System.out.println(userDetailsRepository.findByUsername("akshay"));
                return  "hello";
        }
}
