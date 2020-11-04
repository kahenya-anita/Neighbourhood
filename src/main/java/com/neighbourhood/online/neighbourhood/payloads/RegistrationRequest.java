package com.neighbourhood.online.neighbourhood.payloads;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    private String name;

    private String emailAddress;

    private String password;

    private String neighbourHoodId;
}
