package com.stankowski_strzelka.rbac.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DutyDto {
    private long id;
    private UserDto medical;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int office;
}
