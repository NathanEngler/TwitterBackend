package de.dhbw.twitterbackend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginRequest {
    private String identifier;
    private String password;
}
