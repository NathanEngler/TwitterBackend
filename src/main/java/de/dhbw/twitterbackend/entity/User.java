package de.dhbw.twitterbackend.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private long id;

    @NotBlank(message = "Firstname must not be emptyy")
    @Column(name = "firstname")
    private String firstname;

    @NotBlank(message = "Lastname must not be emptyy")
    @Column(name = "lastname")
    private String lastname;

    @Email(message = "Unvalid E-Mail")
    @NotBlank(message = "E-Mail must not be empty")
    @Column(name = "email" /* , unique = true*/)
    private String email;

    @NotBlank(message = "Username must not be emptyy")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password must not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    @Column(name = "profilepicture")
    private String profilepictureUrl;
}
