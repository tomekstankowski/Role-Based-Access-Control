package com.stankowski_strzelka.rbac.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class LoginDto {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
