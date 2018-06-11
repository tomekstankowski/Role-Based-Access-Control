package com.stankowski_strzelka.rbac.controller;

import com.stankowski_strzelka.rbac.dto.LoginDto;
import com.stankowski_strzelka.rbac.model.Role;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthenticationManager authManager;
    private final UserService service;

    @ModelAttribute("credentials")
    public LoginDto credentialsAttribute() {
        return new LoginDto();
    }

    @GetMapping
    public String getLoginView() {
        return "login";
    }

    @PostMapping
    public String logIn(@Valid @ModelAttribute("credentials") LoginDto credentials,
                        BindingResult bindingResult,
                        HttpSession session) {
        try {
            Authentication authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getEmail(),
                            credentials.getPassword()
                    )
            );
            final User user = service.findByEmail(credentials.getEmail());
            final boolean hasRole = user.getRoles()
                    .stream()
                    .map(Role::getName)
                    .anyMatch(role -> role.equals(credentials.getRole()));
            if (hasRole) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                session.setAttribute("role", credentials.getRole());
                session.setAttribute("id", user.getId());
                return "redirect:/";
            } else {
                bindingResult.reject("no-authority", "You do not have claimed authority");
                return "/login";
            }
        } catch (AuthenticationException ex) {
            bindingResult.reject("bad-credentials", "Invalid e-mail or password");
            return "/login";
        }
    }
}
