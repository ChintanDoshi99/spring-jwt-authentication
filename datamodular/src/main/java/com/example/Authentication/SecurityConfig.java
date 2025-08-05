package com.example.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.websocket.Endpoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtFilter jwtFilter;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		return http.csrf().disable()
						  .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/register","/auth/token", "/swagger-ui/**","/v3/api-docs/**","/actuator/**")
								  							  .permitAll()
								  			                .requestMatchers(EndpointRequest.to("health", "info")).permitAll()
								  							  .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
								  							  .anyRequest().authenticated()
								  				)
						  						.sessionManagement(sess-> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
						  						.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
						  						.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


}

