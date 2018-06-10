package com.stankowski_strzelka.rbac.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
}
