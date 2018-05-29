package com.stankowski_strzelka.dac.controller;

import com.stankowski_strzelka.dac.dto.LoginDto;
import com.stankowski_strzelka.dac.dto.UserRegistrationDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class MainController {

    @GetMapping("/")
    public String root() {
        return "user/index";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }

    @GetMapping("/user/duties")
    public String userDuties(){
        return "user/duties";
    }

    @GetMapping("user/appointments")
    public String userApp(){
        return "user/appointments";
    }
}
