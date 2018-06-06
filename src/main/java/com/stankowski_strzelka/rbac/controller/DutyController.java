package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.DutyService;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DutyController {
    private final DutyService dutyService;
    private final UserService userService;

    @GetMapping("/user/{id}/duties")
    public String duties(Model model, @PathVariable long id) {
        User medical = userService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Użytkownik o id %d nie istnieje", id)));
        List<Duty> duties = dutyService.getDuties(medical);
        model.addAttribute("id", id);
        model.addAttribute("duties", duties);
        return "user/duties";
    }
}
