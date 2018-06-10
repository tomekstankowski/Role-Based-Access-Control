package com.stankowski_strzelka.rbac.repository;

import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DutyRepository extends JpaRepository<Duty, Long> {
    Duty findByMedicalAndStartDateAndEndDate(User medical, LocalDateTime startDate, LocalDateTime endDate);

    List<Duty> findByStartDateGreaterThanAndEndDateLessThan(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT d FROM Duty d WHERE d.medical = :medical AND (" +
            "(d.startDate >= :fromDate and d.startDate < :toDate)" +
            "OR (d.endDate > :fromDate and d.endDate <= :toDate)" +
            "OR (d.startDate >= :fromDate and d.endDate <= :toDate))")
    List<Duty> findExistingDuties(@Param("medical") User medical,
                                  @Param("fromDate") LocalDateTime fromDate,
                                  @Param("toDate") LocalDateTime toDate);

    List<Duty> findByMedical(User medical);
}
