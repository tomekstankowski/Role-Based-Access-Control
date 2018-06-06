package com.stankowski_strzelka.rbac.development;

import com.stankowski_strzelka.rbac.model.*;
import com.stankowski_strzelka.rbac.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
public class InitialDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private DutyRepository dutyRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
        Role userRole = roleRepository.findByName("ROLE_USER");
        User user1 = new User("Test", "Test", "test@test.com",
                passwordEncoder.encode("test"), Collections.singletonList(adminRole));
        User user2 = new User("Jan", "Kowalski", "3@pl",
                passwordEncoder.encode("3"), Collections.singletonList(userRole));
        createUserIfNotFound(user1);
        createUserIfNotFound(user2);

        User medical = userRepository.findByEmail("test@test.com");
        User patient = userRepository.findByEmail("3@pl");
        LocalDateTime start = LocalDateTime.of(2018, 6, 3, 12, 0);
        LocalDateTime end = LocalDateTime.of(2018, 6, 3, 15, 0);

        createDutyIfNotFound(medical, start, end, 1);
        createDutyIfNotFound(medical, start.plusDays(1), end.plusDays(1), 2);

        createAppointmentIfNotFound(medical, patient, start, start.plusMinutes(30));

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    @Transactional
    Duty createDutyIfNotFound(User medical, LocalDateTime start, LocalDateTime end, int office) {
        Duty duty = dutyRepository.findByMedicalAndStartDateAndEndDate(medical, start, end);
        if (duty == null){
            duty = new Duty(medical, start, end, office);
            dutyRepository.save(duty);
        }
        return duty;
    }

    @Transactional
    User createUserIfNotFound(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    Appointment createAppointmentIfNotFound(User medical, User patient, LocalDateTime start, LocalDateTime end){
        Appointment appointment = appointmentRepository.findByMedicalAndPatientAndStartDateAndEndDate(
                medical, patient, start, end
        );
        if (appointment == null){
            appointment = new Appointment(medical, patient, start, end);
            appointmentRepository.save(appointment);
        }
        return appointment;
    }

}
