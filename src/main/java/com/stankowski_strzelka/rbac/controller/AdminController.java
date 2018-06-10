package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.UserDto;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService service;
    private final ModelMapper modelMapper;

    @ModelAttribute("users")
    public List<UserDto> usersAttribute() {
        return service.findAll()
                .stream()
                .map(entity -> modelMapper.map(entity, UserDto.class))
                .collect(Collectors.toList());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USERS')")
    public String getAdminView() {
        return "admin";
    }

    @PostMapping("/users/{id}/delete")
    @PreAuthorize("hasAuthority('DELETE_USERS')")
    public String deleteUser(@PathVariable long id) {
        service.deleteUser(id);
        return "redirect:/admin?deleted";
    }
}
