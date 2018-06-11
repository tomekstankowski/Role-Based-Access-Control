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

        Privilege createDuties = createPrivilegeIfNotFound("CREATE_DUTIES");
        Privilege readDuties = createPrivilegeIfNotFound("READ_DUTIES");
        Privilege deleteDuties = createPrivilegeIfNotFound("DELETE_DUTIES");
        Privilege readUsers = createPrivilegeIfNotFound("READ_USERS");
        Privilege updateUsers = createPrivilegeIfNotFound("UPDATE_USERS");
        Privilege deleteUsers = createPrivilegeIfNotFound("DELETE_USERS");
        Privilege createAppointments = createPrivilegeIfNotFound("CREATE_APPOINTMENTS");
        Privilege readAppointments = createPrivilegeIfNotFound("READ_APPOINTMENTS");
        Privilege readMedicalAppointments = createPrivilegeIfNotFound("READ_MEDICAL_APPOINTMENTS");


        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", Arrays.asList(readUsers, updateUsers, deleteUsers));
        Role userRole = createRoleIfNotFound("ROLE_PATIENT", Arrays.asList(createAppointments, readAppointments));
        Role medicalRole = createRoleIfNotFound("ROLE_MEDICAL", Arrays.asList(createDuties, readDuties, deleteDuties, readMedicalAppointments));

        User admin = new User("Janusz", "Admiński", "janusz@admin.pl",
                passwordEncoder.encode("admin"), Arrays.asList(adminRole, medicalRole));
        User medical = new User("Dr Jan", "Lekarz", "jan@lekarz.pl",
                passwordEncoder.encode("lekarz"), Arrays.asList(userRole, medicalRole));
        User medical2 = new User("Dr Michał", "Kowalski", "michal@lekarz.pl",
                passwordEncoder.encode("lekarz"), Arrays.asList(userRole, medicalRole));
        User patient = new User("Zenon", "Pacjent", "zenon@pacjent.pl",
                passwordEncoder.encode("pacjent"), Collections.singleton(userRole));
        createUserIfNotFound(admin);
        createUserIfNotFound(medical);
        createUserIfNotFound(medical2);
        createUserIfNotFound(patient);

        medical = userRepository.findByEmail("jan@lekarz.pl");
        medical2 = userRepository.findByEmail("michal@lekarz.pl");
        patient = userRepository.findByEmail("zenon@pacjent.pl");

        LocalDateTime start = LocalDateTime.of(2018, 6, 3, 12, 0);
        LocalDateTime end = LocalDateTime.of(2018, 6, 3, 15, 0);

        createDutyIfNotFound(medical, start, end, 1);
        createDutyIfNotFound(medical2, start, end.plusHours(2), 5);
        createDutyIfNotFound(medical, start.plusDays(1), end.plusDays(1), 2);

        createAppointmentIfNotFound(medical, patient, start);
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
        if (duty == null) {
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

    private Appointment createAppointmentIfNotFound(User medical, User patient, LocalDateTime start) {
        Appointment appointment = appointmentRepository.findByMedicalAndPatientAndStartDate(
                medical, patient, start
        );
        if (appointment == null) {
            appointment = new Appointment(medical, patient, start);
            appointmentRepository.save(appointment);
        }
        return appointment;
    }

}
