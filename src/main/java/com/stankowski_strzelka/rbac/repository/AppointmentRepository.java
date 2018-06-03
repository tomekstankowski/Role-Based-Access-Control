package com.stankowski_strzelka.rbac.repository;

import com.stankowski_strzelka.rbac.model.Appointment;
import com.stankowski_strzelka.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Appointment findByMedicalAndPatientAndStartDateAndEndDate(
            User medical, User patient, LocalDateTime startDate, LocalDateTime endDate
    );
}