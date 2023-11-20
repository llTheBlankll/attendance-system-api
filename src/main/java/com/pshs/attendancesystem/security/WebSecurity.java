package com.pshs.attendancesystem.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    /**
     * Generates the security filter chain for the HTTP security.
     *
     * @param httpSecurity the HttpSecurity object representing the security configuration
     * @return the SecurityFilterChain object representing the security filter chain
     * @throws Exception if an exception occurs during the generation of the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .httpBasic(Customizer.withDefaults())
            .cors(Customizer.withDefaults())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/api/v1/rfid/**")
                    .hasAnyRole(Privilege.PRINCIPAL.name())
                    .requestMatchers("/api/v1/**")
                    .hasAnyRole(Privilege.PRINCIPAL.name(), Privilege.TEACHER.name())
                    .requestMatchers("/websocket/**")
                    .hasAnyRole(Privilege.RFID_DEVICE.name())
                    .anyRequest()
                    .authenticated());

        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsManager userDetailsManager(BCryptPasswordEncoder encoder) {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.withUsername("principal")
            .password(encoder.encode("1234"))
            .roles(Privilege.PRINCIPAL.name())
            .build());

        manager.createUser(User.withUsername("teacher")
            .password(encoder.encode("1234"))
            .roles(Privilege.TEACHER.name())
            .build());

        manager.createUser(
            User.withUsername("esp32")
                .password(encoder.encode("1234"))
                .roles(Privilege.RFID_DEVICE.name())
                .build()
        );

        return manager;
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private enum Privilege {
        PRINCIPAL,
        TEACHER,
        RFID_DEVICE
    }
}
