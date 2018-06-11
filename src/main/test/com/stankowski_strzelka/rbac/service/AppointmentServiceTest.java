package com.stankowski_strzelka.rbac.service;


import com.stankowski_strzelka.rbac.model.Appointment;
import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.AppointmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AppointmentServiceTest {

    private  List<Duty> VALID_DUTIES_LIST;
    private  List<Appointment> VALID_APPOINTMENT_LIST_1;
    private  List<Appointment> VALID_APPOINTMENT_LIST_2;
    private  User VALID_MEDICAL_1;
    private  User VALID_MEDICAL_2;
    private  User VALID_PATIENT;
    private  List<Appointment> VALID_RESPONSE;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DutyService dutyService;

    @InjectMocks
    private AppointmentService appointmentService;


    @BeforeAll
    void setUp() {
        MockitoAnnotations.initMocks(this);
        LocalDateTime start = LocalDateTime.of(2018, 8, 6, 12, 0);
        VALID_MEDICAL_1 = new User("John", "Travolta", "abc@gmail.com",
                "1234", Collections.emptySet());
        VALID_MEDICAL_2 = new User("Micheal", "Jordan", "basketball@gmail.com",
                "ilovebasketball", Collections.emptySet());
        VALID_PATIENT = new User("Tom", "Dikson", "12@gmail.com", "admin",
                Collections.emptySet());
        VALID_DUTIES_LIST = Arrays.asList(
                new Duty(VALID_MEDICAL_1, start, start.plusHours(2), 100),
                new Duty(VALID_MEDICAL_2, start, start.plusMinutes(90), 100),
                new Duty(VALID_MEDICAL_1, start.plusDays(2).plusHours(1), start.plusDays(2).plusHours(2), 100)
        );

        VALID_APPOINTMENT_LIST_1 = Arrays.asList(
          new Appointment(VALID_MEDICAL_1, VALID_PATIENT, start),
          new Appointment(VALID_MEDICAL_1, VALID_PATIENT, start.plusHours(1)),
          new Appointment(VALID_MEDICAL_1, VALID_PATIENT, start.plusDays(2).plusMinutes(90))
        );

        VALID_APPOINTMENT_LIST_2 = Collections.singletonList(
                new Appointment(VALID_MEDICAL_2, VALID_PATIENT, start.plusMinutes(30))
        );

        VALID_RESPONSE = new ArrayList<>(Arrays.asList(
                new Appointment(VALID_MEDICAL_1, null, start.plusMinutes(30)),
                new Appointment(VALID_MEDICAL_1, null, start.plusMinutes(90)),
                new Appointment(VALID_MEDICAL_1, null, start.plusDays(2).plusHours(1)),
                new Appointment(VALID_MEDICAL_2, null, start),
                new Appointment(VALID_MEDICAL_2, null, start.plusHours(1))
        ));
        Mockito.when(appointmentRepository.findAllByMedical(VALID_MEDICAL_1)).thenReturn(VALID_APPOINTMENT_LIST_1);
        Mockito.when(appointmentRepository.findAllByMedical(VALID_MEDICAL_2)).thenReturn(VALID_APPOINTMENT_LIST_2);
        Mockito.when(dutyService.getAllDuties()).thenReturn(VALID_DUTIES_LIST);

    }

    @Test
    void shouldReturnCorrectAppointmentList() {
        List<Appointment> response =  appointmentService.getAllPossibleAppointments();

        response.sort(Comparator.comparing(Appointment::getStartDate));
        VALID_RESPONSE.sort(Comparator.comparing(Appointment::getStartDate));

        Assertions.assertEquals(VALID_RESPONSE, response);
    }
}
