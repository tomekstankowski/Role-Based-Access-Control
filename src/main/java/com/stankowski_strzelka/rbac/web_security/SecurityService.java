package com.stankowski_strzelka.rbac.web_security;

import com.stankowski_strzelka.rbac.model.Privilege;
import com.stankowski_strzelka.rbac.model.Role;
import com.stankowski_strzelka.rbac.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class SecurityService {
    private final RoleRepository roleRepository;

    public boolean hasPrivilege(String privilege) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpSession session = attr.getRequest().getSession(false);
        if (auth != null && auth.isAuthenticated() && session != null) {
            final String currentRole = (String) session.getAttribute("role");
            if (currentRole != null) {
                final Role role = roleRepository.findByName(currentRole);
                return role.getPrivileges()
                        .stream()
                        .map(Privilege::getName)
                        .anyMatch(p -> p.equals(privilege));
            }
        }
        return false;
    }
}
