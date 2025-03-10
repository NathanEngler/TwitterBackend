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
        //response.put("password", user.getPassword());
        return response;
    }
    public void changePassword(long id, String password) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setPassword(passwordEncoder.encode(password));
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    public boolean verifyPassword(long userId, String oldPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        // Vergleiche das eingegebene alte Passwort mit dem gespeicherten Passwort
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    public void changeEmail(long id, String email) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setEmail(email);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    public void changeFirstname(long id, String firstname) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setFirstname(firstname);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    public void changeLastname(long id, String lastname) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setLastname(lastname);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    public void changeUsername(long id, String username) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setUsername(username);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);
        }
    }
    public void changeProfilepicture(long id, String profilepictureUrl){
        if(userRepository.findById(id).isPresent()) {
            userRepository.findById(id).get().setProfilepictureUrl(profilepictureUrl);
            userRepository.save(userRepository.findById(id).get());
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);

        }
    }

    //Testdaten löschen
    public void deleteAll(){
        userRepository.deleteAll();
    }

    public void deleteUserById(long id) {
        if(userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with ID: " + id);

        }
    }


}
