package com.stankowski_strzelka.rbac.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class DutyCreationDto {
    private UserDto medical;
    @NotNull
    private LocalDateTime fromDate;
    @NotNull
    private LocalDateTime toDate;
    @Min(1)
    private int office;
}
