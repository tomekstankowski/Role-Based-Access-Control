package com.stankowski_strzelka.dac.dto;

import com.stankowski_strzelka.dac.validator.FieldMatch;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;


@Getter
@Setter
public class LoginDto {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
