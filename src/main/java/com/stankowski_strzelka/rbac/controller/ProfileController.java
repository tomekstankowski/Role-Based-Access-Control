package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.UserUpdateDto;
import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/profile/{id}")
@RequiredArgsConstructor
public class ProfileController {
    private final UserService service;
    private final ModelMapper modelMapper;

    @ModelAttribute("user")
    public UserUpdateDto userAttribute(@PathVariable long id, Model model) {
        final User user = service.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id %d could not be found"));
        model.addAttribute("email", user.getEmail());
        return modelMapper.map(user, UserUpdateDto.class);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_USERS') OR principal.username == #model.get('email')")
    public String getProfileView(ModelMap model) {
        return "profile";
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('UPDATE_USERS') OR principal.username == #model.get('email')")
    public String updateProfile(@PathVariable long id,
                                @ModelAttribute("user") @Valid UserUpdateDto user,
                                BindingResult bindingResult,
                                ModelMap model) {
        if (bindingResult.hasErrors()) {
            return "/profile";
        }
        service.updateUser(user);
        return String.format("redirect:/profile/%d?success", id);
    }
}
