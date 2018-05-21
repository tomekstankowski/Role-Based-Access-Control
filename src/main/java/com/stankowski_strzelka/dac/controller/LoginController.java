package com.stankowski_strzelka.dac.controller;

import com.stankowski_strzelka.dac.dto.LoginDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/login")
public class LoginController {

    @ModelAttribute("loginUser")
    public LoginDto userLoginDto() {
        return new LoginDto();
    }

    @PostMapping
    public String userLogin(@ModelAttribute("loginUser") @Valid LoginDto userDto,
                            BindingResult result){
        System.out.println("im here");
        return "login";
    }

    @GetMapping
    public String login(Model model) {
        return "login";
    }
}
