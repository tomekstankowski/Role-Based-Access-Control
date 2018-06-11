package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.dto.UserRegistrationDto;
import com.stankowski_strzelka.rbac.dto.UserUpdateDto;
import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.Privilege;
import com.stankowski_strzelka.rbac.model.Role;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.RoleRepository;
import com.stankowski_strzelka.rbac.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
               getAuthorities(user.getRoles()));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(UserRegistrationDto registration) {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER")));

        return userRepository.save(user);
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User deleteUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(userNotFoundException(id));
        userRepository.delete(user);
        return user;
    }

    public User updateUser(@NonNull final UserUpdateDto body) {
        User entity = userRepository.findById(body.getId())
                .orElseThrow(userNotFoundException(body.getId()));
        modelMapper.map(body, entity);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity = userRepository.save(entity);
        return entity;
    }
    private Collection<? extends GrantedAuthority> getAuthorities(
            Collection<Role> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<Role> roles) {

        List<Privilege> collection = new ArrayList<>();
        for (Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        return collection.stream()
                .map(Privilege::getName)
                .collect(Collectors.toList());
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {

        return privileges.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private Supplier<ResourceNotFoundException> userNotFoundException(long id) {
        return () -> new ResourceNotFoundException("User with id %d could not be found");
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(authentication.getName());
    }
    public String getUserRole(){
        final ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        final HttpSession session = attr.getRequest().getSession(false);
        return (String) session.getAttribute("role");
    }
}
