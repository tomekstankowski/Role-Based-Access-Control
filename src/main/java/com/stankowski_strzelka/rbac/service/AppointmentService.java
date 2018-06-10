package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.dto.AppointmentDto;
import com.stankowski_strzelka.rbac.model.Appointment;
import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DutyService dutyService;

    @Autowired
    private UserService userService;

    private final static int APPOINTMENT_DURATION = 30;

    public List<Appointment> getAllPatientAppointments(User patient) {
        return appointmentRepository.findAllByPatient(patient)
                .stream().sorted(Comparator.comparing(Appointment::getStartDate))
                .collect(toList());
    }

    //FIXME
    //throw correct exception
    public Appointment saveAppointment(AppointmentDto appointmentDto) throws Exception {
        Appointment appointment = new Appointment(
                userService.findById(appointmentDto.getMedicalId()).orElseThrow(Exception::new),
                userService.getCurrentUser(),
                appointmentDto.getStartDate()
        );
        appointmentRepository.save(appointment);
        return appointment;
    }

    public List<Appointment> getAllPossibleAppointments() {
        List<Appointment> finalAppointments = new ArrayList<>();
        List<Duty> duties = dutyService.getAllDuties();
        for (Duty duty : duties) {
            List<Appointment> appointments = appointmentRepository.findAllByMedical(duty.getMedical());
            List<Appointment> availableAppointments = getAppointmentsFromDuty(duty);

            for (Appointment appointment : availableAppointments) {
                boolean found = appointments.stream().anyMatch(
                        app -> app.getStartDate().equals(appointment.getStartDate()) &&
                                app.getEndDate().equals(appointment.getEndDate())
                );
                if (!found)
                    finalAppointments.add(appointment);
            }
        }
        return finalAppointments.stream().sorted(Comparator.comparing(Appointment::getStartDate))
                .collect(toList());
    }

    private List<Appointment> getAppointmentsFromDuty(Duty duty) {
        List<Appointment> appointments = new ArrayList<>();
        for (int i = 0; i < getNumberOfPossibleAppointments(duty.getStartDate(), duty.getEndDate()); i++) {
            appointments.add(
                    new Appointment(
                            duty.getMedical(),
                            null,
                            duty.getStartDate().plusMinutes(APPOINTMENT_DURATION * i)
                    )
            );
        }
        return appointments;
    }

    private int getNumberOfPossibleAppointments(LocalDateTime start, LocalDateTime end) {
        return (int) (start.until(end, ChronoUnit.MINUTES) / 30);
    }
}
