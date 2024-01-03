package com.pshs.attendancesystem.security;

import com.pshs.attendancesystem.enums.Roles;
import com.pshs.attendancesystem.security.jwt.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurity {

	private final AuthenticationProvider authenticationProvider;
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	@Value("${api.root}")
	private String baseUrl;

	public WebSecurity(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.authenticationProvider = authenticationProvider;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	/**
	 * Generates the security filter chain for the HTTP security.
	 *
	 * @param http the HttpSecurity object representing the security configuration
	 * @return the SecurityFilterChain object representing the security filter chain
	 * @throws Exception if an exception occurs during the generation of the security filter chain
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable) // ! Temporarily disable csrf.
			.sessionManagement(session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests(authorize ->
				authorize
					.requestMatchers(baseUrl + "/rfid/**")
					.hasAnyRole(Roles.PRINCIPAL.name())

					.requestMatchers("/websocket/**")
					.hasAnyRole(Roles.OTHER.name())

					.requestMatchers(baseUrl + "/user/**")
					.hasAnyRole(Roles.ADMIN.name())

					.requestMatchers(baseUrl + "/**")
					.hasAnyRole(Roles.TEACHER.name(), Roles.PRINCIPAL.name())

					.requestMatchers("/auth/**")
					.permitAll()

					.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/redoc")
					.permitAll()

					.requestMatchers("/css/**", "/js/**", "/img/**", "/error/**", "/favicon.ico")
					.permitAll()

					.anyRequest()
					.authenticated()
			)
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.build();
	}

	@Bean
	CorsConfigurationSource corsConfiguration() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(List.of("*"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
