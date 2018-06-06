package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.dto.DutyCreationDto;
import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.DutyRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DutyService {

    private final DutyRepository repository;
    private final ModelMapper mapper;

    public List<Duty> getDuties(@NonNull final User medical) {
        return repository.findByMedical(medical);
    }

    public List<Duty> getDuties(@NonNull final LocalDateTime fromDate, @NonNull final LocalDateTime toDate) {
        return repository.findByStartDateGreaterThanAndEndDateLessThan(fromDate, toDate);
    }

    public Duty createDuty(@NonNull final DutyCreationDto body) {
        Duty entity = mapper.map(body, Duty.class);
        entity = repository.save(entity);
        return entity;
    }
}
