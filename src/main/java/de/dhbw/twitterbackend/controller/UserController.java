package de.dhbw.twitterbackend.controller;

import de.dhbw.twitterbackend.entity.User;
import de.dhbw.twitterbackend.exceptions.UserNotFoundException;
import de.dhbw.twitterbackend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public User insert(@Valid @RequestBody User user){
        return userService.insert(user);
    }


    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getUserProfile(@PathVariable Long id) {
        Long authenticatedUserId = getAuthenticatedUserId();
        if (!authenticatedUserId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You only see your own profile!");
        }
        return ResponseEntity.ok(userService.getUserProfile(id));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Map<String, String>> updateUserField(
            @PathVariable long id,
            @RequestBody Map<String, String> request
    ) {
        Long authenticatedUserId = getAuthenticatedUserId();
        if (!authenticatedUserId.equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message", "You only can change your own data!"));
        }

        String field = request.get("field");
        String value = request.get("value");

        if (field == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "Field and value are required"));
        }

        switch (field) {
            case "email":
                userService.changeEmail(id, value);
                break;
            case "firstname":
                userService.changeFirstname(id, value);
                break;
            case "lastname":
                userService.changeLastname(id, value);
                break;
            case "username":
                userService.changeUsername(id, value);
                break;
            case "password":
                String oldPassword = request.get("oldPassword"); //
                if (oldPassword == null || oldPassword.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of("message", "Old password is required"));
                }
                if (!userService.verifyPassword(id, oldPassword)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Old password is incorrect"));
                }
                userService.changePassword(id, value);
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("message", "Invalid field"));
        }

        return ResponseEntity.ok(Map.of("message", field + " successfully changed!"));
    }

    @PutMapping("/{id}/upload-profile-picture")
    public ResponseEntity<Map<String, String>> uploadProfilePicture(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            // Speicherpfad für das Bild setzen
            String uploadDir = "uploads/";
            String filename = "user-" + id + "-profile.jpg";
            Path filePath = Paths.get(uploadDir + filename);

            // Sicherstellen, dass der Ordner da ist
            Files.createDirectories(Paths.get(uploadDir));
            // Datei speichern
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL generieren, die in der DB gespeichert wird
            String profilepictureUrl = "/uploads/" + filename;

            // In der Datenbank speichern
            userService.changeProfilepicture(id, profilepictureUrl);

            // Gültige JSON-Antwort zurückgeben
            return ResponseEntity.ok(Map.of("profilepictureUrl", profilepictureUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Fehler beim Speichern: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Unbekannter Fehler: " + e.getMessage()));
        }
    }

    @GetMapping("/uploads/{filename}")
    public ResponseEntity<Resource> getProfilePicture(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads/").resolve(filename).normalize();
            System.out.println("Versuche, Datei zu laden: " + filePath.toAbsolutePath());

            Resource resource = new UrlResource(filePath.toUri());
            System.out.println("Resource existiert: " + resource.exists());
            System.out.println("Resource ist lesbar: " + resource.isReadable());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok().body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUserById(@PathVariable long id) {
        try {
            // Überprüfe, ob der Benutzer existiert
            if (!userService.userExists(id)) {
                Map<String, String> response = new HashMap<>();
                response.put("status", "404");
                response.put("message", "Benutzer mit ID " + id + " nicht gefunden.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Lösche den Benutzer
            userService.deleteUserById(id);

            // Erfolgsmeldung zurückgeben
            Map<String, String> response = new HashMap<>();
            response.put("status", "200");
            response.put("message", "Benutzer mit ID " + id + " wurde erfolgreich gelöscht.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Fehlermeldung zurückgeben
            Map<String, String> response = new HashMap<>();
            response.put("status", "500");
            response.put("message", "Fehler beim Löschen des Benutzers: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    /* Testdaten alle löschen
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAll() {
        userService.deleteAll();
        return ResponseEntity.ok("Alle user gelöscht");
    }

     */

    //Hilfsmethode
    private Long getAuthenticatedUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return Long.parseLong(userDetails.getUsername()); // "Username" = Userid
        }
        return null;
    }

}
