package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String root() {
        return "user/index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("user/appointments")
    public String userApp(){
        return "user/appointments";
    }
}
