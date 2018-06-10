package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.UserUpdateDto;
import com.stankowski_strzelka.rbac.exception.BadRequestException;
import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/user/{id}/account")
@RequiredArgsConstructor
public class AccountController {
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
    public String getAccountView(ModelMap model) {
        return "user/account";
    }

    @PostMapping
    public String handleFormSubmit(@PathVariable long id,
                                   @RequestParam String action,
                                   @ModelAttribute("user") @Valid UserUpdateDto user,
                                   BindingResult bindingResult,
                                   ModelMap modelMap,
                                   HttpServletRequest request,
                                   Authentication authentication) throws ServletException {
        if (action.equals("update")) {
            return updateAccount(id, user, bindingResult, modelMap);
        } else if (action.equals("delete")) {
            return deleteAccount(id, modelMap, request, authentication);
        } else {
            throw new BadRequestException("Unexpected 'action' parameter value");
        }
    }

    @PreAuthorize("hasAuthority('UPDATE_USERS') OR principal.username == #modelMap.get('email')")
    private String updateAccount(@PathVariable long id,
                                 @ModelAttribute("user") @Valid UserUpdateDto user,
                                 BindingResult bindingResult,
                                 ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return "user/account";
        }
        service.updateUser(user);
        return String.format("redirect:/user/%d/account?success", id);
    }

    @PreAuthorize("hasAuthority('DELETE_USERS') OR principal.username == #model.get('email')")
    private String deleteAccount(@PathVariable long id, ModelMap model, HttpServletRequest request, Authentication authentication)
            throws ServletException {
        service.deleteUser(id);
        if (authentication.getName().equals(model.get("email"))) {
            request.logout();
            return "redirect:/login";
        }
        return "redirect:/admin?deleted";
    }
}
