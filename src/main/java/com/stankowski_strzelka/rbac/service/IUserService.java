package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.dto.UserRegistrationDto;
import com.stankowski_strzelka.rbac.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
