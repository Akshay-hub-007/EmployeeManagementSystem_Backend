package com.employee.demo.dto;

import com.employee.demo.models.ROLE;
import com.employee.demo.models.Users;
import org.springframework.stereotype.Component;

@Component
public class UserResponse {
    public UserResponse()
    {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    private String message;
    private Users user;

    public UserResponse(String message, Users user) {
        this.message = message;
        this.user = user;
    }

}
