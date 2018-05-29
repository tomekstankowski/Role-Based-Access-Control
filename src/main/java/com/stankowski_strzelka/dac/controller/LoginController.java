package com.stankowski_strzelka.dac.controller;

import com.stankowski_strzelka.dac.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    @ModelAttribute("loginUser")
    public LoginDto userLoginDto() {
        return new LoginDto();
    }

    @GetMapping
    public String login(Model model) {
        return "login";
    }
}
