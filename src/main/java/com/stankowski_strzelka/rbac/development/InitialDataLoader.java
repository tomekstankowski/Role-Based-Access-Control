package com.stankowski_strzelka.rbac.development;

import com.stankowski_strzelka.rbac.model.*;
import com.stankowski_strzelka.rbac.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class InitialDataLoader implements
        ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final DutyRepository dutyRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
        Privilege createDuties = createPrivilegeIfNotFound("CREATE_DUTIES");
        Privilege readDuties = createPrivilegeIfNotFound("READ_DUTIES");
        Privilege deleteDuties = createPrivilegeIfNotFound("DELETE_DUTIES");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));
        Role medicalRole = createRoleIfNotFound("ROLE_MEDICAL", Arrays.asList(createDuties, readDuties, deleteDuties));

        User user1 = new User("Test", "Test", "test@test.com",
                passwordEncoder.encode("test"), Collections.singletonList(userRole));
        User user2 = new User("Jan", "Kowalski", "3@pl",
                passwordEncoder.encode("3"), Arrays.asList(userRole, medicalRole));
        createUserIfNotFound(user1);
        createUserIfNotFound(user2);

        User medical = userRepository.findByEmail("test@test.com");
        User patient = userRepository.findByEmail("3@pl");
        LocalDateTime start = LocalDateTime.of(2018, 6, 3, 12, 0);
        LocalDateTime end = LocalDateTime.of(2018, 6, 3, 15, 0);

        createDutyIfNotFound(medical, start, end, 1);
        createDutyIfNotFound(medical, start.plusDays(1), end.plusDays(1), 2);

        createAppointmentIfNotFound(medical, patient, start, start.plusMinutes(30));
    }

    private Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    private Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    private Duty createDutyIfNotFound(User medical, LocalDateTime start, LocalDateTime end, int office) {
        Duty duty = dutyRepository.findByMedicalAndStartDateAndEndDate(medical, start, end);
        if (duty == null){
            duty = new Duty(medical, start, end, office);
            dutyRepository.save(duty);
        }
        return duty;
    }

    private User createUserIfNotFound(User user) {
        if (userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
        }
        return user;
    }

    private Appointment createAppointmentIfNotFound(User medical, User patient, LocalDateTime start, LocalDateTime end) {
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
