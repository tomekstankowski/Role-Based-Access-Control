package com.stankowski_strzelka.rbac.web_security;

import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final IUserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException {
        final HttpSession session = request.getSession(false);
        final String email = authentication.getName();
        final User user = userService.findByEmail(email);
        if (user != null) {
            session.setAttribute("user", user);
        }
    }
}