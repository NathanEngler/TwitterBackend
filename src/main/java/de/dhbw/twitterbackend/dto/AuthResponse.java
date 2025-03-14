package de.dhbw.twitterbackend.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class AuthResponse {
    //Prinzipiell nicht notwendig für nur ein Attribut, aber bessere Erweiterbarkeit
    private String token;
}
