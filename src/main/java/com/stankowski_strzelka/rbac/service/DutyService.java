package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.dto.DutyCreationDto;
import com.stankowski_strzelka.rbac.exception.ConflictException;
import com.stankowski_strzelka.rbac.exception.ResourceNotFoundException;
import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.DutyRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

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

    public Duty createDuty(@NonNull final DutyCreationDto body, @NonNull final User medical) {
        List<Duty> exisitingDuties = repository.findExistingDuties(medical, body.getStartDate(), body.getEndDate());
        if (exisitingDuties.isEmpty()) {
            Duty entity = mapper.map(body, Duty.class);
            entity.setMedical(medical);
            entity = repository.save(entity);
            return entity;
        }
        throw new ConflictException("Duty start date or end date conflicts with already existing duties");
    }

    public Duty deleteDuty(final long id) {
        final Duty duty = repository.findById(id)
                .orElseThrow(dutyNotFoundException(id));
        repository.delete(duty);
        return duty;
    }
    public List<Duty> getAllDuties(){
        return repository.findAll();
    }

    private Supplier<ResourceNotFoundException> dutyNotFoundException(long id) {
        return () -> new ResourceNotFoundException(String.format("Duty with id %d could not be found", id));
    }
}
