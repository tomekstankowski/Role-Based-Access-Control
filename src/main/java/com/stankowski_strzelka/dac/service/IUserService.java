package com.stankowski_strzelka.dac.service;

import com.stankowski_strzelka.dac.dto.UserRegistrationDto;
import com.stankowski_strzelka.dac.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface IUserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
