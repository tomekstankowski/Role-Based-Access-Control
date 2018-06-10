package com.stankowski_strzelka.rbac.dto;

import com.stankowski_strzelka.rbac.validator.DateNotPast;
import com.stankowski_strzelka.rbac.validator.DatesChronological;
import com.stankowski_strzelka.rbac.validator.MaxDatesDifference;
import com.stankowski_strzelka.rbac.validator.MinDatesDifference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@DatesChronological(first = "startDate", second = "endDate", message = "Duty start date must be before end date")
@MaxDatesDifference(first = "startDate", second = "endDate", millis = 1000 * 60 * 60 * 16, message = "Duty must not be longer than 16 hours")
@MinDatesDifference(first = "startDate", second = "endDate", millis = 1000 * 60 * 60, message = "Duty must not be shorter than 1 hour")
public class DutyCreationDto {

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @DateNotPast
    private LocalDateTime startDate;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endDate;

    @Min(1)
    @Max(200)
    private int office;
}
