package de.dhbw.twitterbackend.controller;

import de.dhbw.twitterbackend.dto.AuthResponse;
import de.dhbw.twitterbackend.dto.LoginRequest;
import de.dhbw.twitterbackend.entity.User;
import de.dhbw.twitterbackend.repository.UserRepository;
import de.dhbw.twitterbackend.config.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // Nutzer anhand von Email oder Username suchen
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getIdentifier());
        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUsername(loginRequest.getIdentifier());
        }

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Falsche Zugangsdaten!"));
        }

        User user = userOptional.get();
        if (!new BCryptPasswordEncoder().matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Falsche Zugangsdaten!"));
        }

        //  Hier wird jetzt das Token mit dem User generiert
        String token = jwtTokenProvider.createToken(user);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}


