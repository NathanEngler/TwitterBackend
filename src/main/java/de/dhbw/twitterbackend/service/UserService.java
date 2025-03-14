package de.dhbw.twitterbackend.service;

import de.dhbw.twitterbackend.entity.User;
import de.dhbw.twitterbackend.exceptions.EmailAlreadyExistsException;
import de.dhbw.twitterbackend.exceptions.UserNotFoundException;
import de.dhbw.twitterbackend.exceptions.UsernameAlreadyExistsException;
import de.dhbw.twitterbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User insert(User user){
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("There already is a user with this email!");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("There already is a user with this username!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Map<String, Object> getUserProfile(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User nicht gefunden"));

        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("firstname", user.getFirstname());
        response.put("lastname", user.getLastname());
        response.put("email", user.getEmail());
        response.put("username", user.getUsername());
        response.put("profilePictureUrl", user.getProfilepictureUrl());
        //response.put("password", user.getPassword()); Passwort wird nur mit **** angezeigt
        return response;
    }
    // Allgemeine Methode zum Aktualisieren eines Benutzerfelds
    private void updateUserField(long id, Consumer<User> updateAction) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        updateAction.accept(user);
        userRepository.save(user);
    }

    public void changePassword(long id, String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        updateUserField(id, user -> user.setPassword(passwordEncoder.encode(newPassword)));
    }

    // Altes Passwort verifizieren
    public boolean verifyPassword(long userId, String oldPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void changeEmail(long id, String email) {
        updateUserField(id, user -> user.setEmail(email));
    }

    public void changeFirstname(long id, String firstname) {
        updateUserField(id, user -> user.setFirstname(firstname));
    }

    public void changeLastname(long id, String lastname) {
        updateUserField(id, user -> user.setLastname(lastname));
    }

    public void changeUsername(long id, String username) {
        updateUserField(id, user -> user.setUsername(username));
    }

    public void changeProfilepicture(long id, String profilepictureUrl){
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setProfilepictureUrl(profilepictureUrl);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);

        }
    }

    /*
    //Testdaten löschen
    public void deleteAll(){
        userRepository.deleteAll();
    }

     */

    public void deleteUserById(long id) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);

        }
    }
    public boolean userExists(long id) {
        return userRepository.existsById(id);
    }
}
