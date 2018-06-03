package com.stankowski_strzelka.rbac.repository;

import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface DutyRepository extends JpaRepository<Duty, Long> {
    Duty findByMedicalAndStartDateAndEndDate(User medical, LocalDateTime startDate, LocalDateTime endDate);
}
