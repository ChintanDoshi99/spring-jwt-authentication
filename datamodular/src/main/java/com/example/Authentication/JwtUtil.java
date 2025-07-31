package com.example.Authentication;


	
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private String key = "my-secret-key-which-should-be-long-enough-and-base64";  
	private SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
// can use env variable instead
// final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256); // ✅ Correct

	public String generateToken(String username) {
		return Jwts.builder().setSubject(username)
							 .setIssuedAt(new Date(System.currentTimeMillis()))
							 .setExpiration(new Date(System.currentTimeMillis()+1000*60*60))
							 .signWith(SignatureAlgorithm.HS256,secretKey).compact();
	}
	public String extractUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}	
	
	 public boolean validateToken(String token,UserDetails userDetails) {
		 String username= extractUsername(token);
		 return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	 }
	private boolean isTokenExpired(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().before(new Date());
	}
		
}

