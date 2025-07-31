package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Authentication.AuthRequest;
import com.example.Authentication.AuthResponse;
import com.example.Authentication.CustomUserDetailsService;
import com.example.Authentication.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@PostMapping("/token")
	public ResponseEntity<AuthResponse> generateToken (@RequestBody AuthRequest request){
		try {
			System.out.println("Username: " + request.getUsername());
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()) );
			final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
			final String token = jwtUtil.generateToken(userDetails.getUsername());
			return ResponseEntity.ok(new AuthResponse(token));	
		}
		catch(Exception e) {
			 System.out.println("Auth failed: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
		}
	}
	
}
