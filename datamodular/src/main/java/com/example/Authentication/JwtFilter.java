package com.example.Authentication;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	private final Map<String,Bucket> userBuckets = new ConcurrentHashMap<>();
	
	private Bucket createNewBucket() {
		return Bucket4j.builder().addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)))).build();
	}
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");
		System.out.println("bearer token id" + authHeader);
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			String token=authHeader.substring(7);
			String username = jwtUtil.extractUsername(token);
			if(username != null && SecurityContextHolder.getContext().getAuthentication()==null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if(jwtUtil.validateToken(token, userDetails)) {
					
					Bucket bucket =userBuckets.computeIfAbsent(username, k -> createNewBucket());
					System.out.println("userbucket mapp" + userBuckets );
					if(!bucket.tryConsume(1)) {
						response.setStatus(429);
						response.getWriter().write("Rate limit exceeded for user:"+username);
						return;
					}
					
					
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( userDetails,null,userDetails.getAuthorities());
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
				}
			}
		}
		filterChain.doFilter(request, response);
		
	}

}
