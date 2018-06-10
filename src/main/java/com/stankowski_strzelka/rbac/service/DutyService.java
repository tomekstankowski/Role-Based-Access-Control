package com.stankowski_strzelka.rbac.service;

import com.stankowski_strzelka.rbac.model.Duty;
import com.stankowski_strzelka.rbac.model.User;
import com.stankowski_strzelka.rbac.repository.DutyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DutyService {

    private final DutyRepository dutyRepository;

    @Autowired
    public DutyService(DutyRepository dutyRepository) {
        this.dutyRepository = dutyRepository;
    }

    public List<Duty> getAllDuties(){
        return dutyRepository.findAll();
    }
}
