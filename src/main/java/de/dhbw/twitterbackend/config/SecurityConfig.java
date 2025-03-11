package de.dhbw.twitterbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http

                .cors(cors -> cors.disable()) // Falls du CORS brauchst, kannst du es aktivieren
                .csrf(csrf -> csrf.disable()) // Kein CSRF-Schutz für REST APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/login", "/api/user/register").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/**").permitAll()

                        .requestMatchers("/uploads/**").permitAll()  // Erlaubt Zugriff auf /uploads mit Bildern

                        // Login und Registrierung sind öffentlich
                        .requestMatchers("/api/user/**").authenticated() // Alle User-Änderungen benötigen Authentifizierung
                        .anyRequest().authenticated() // Alle anderen Endpunkte erfordern Auth
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Keine HTTP-Session, nur JWT
                .addFilterBefore(new JwtTokenFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


}