package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TestUrl {
	@GetMapping("/test")
	public ResponseEntity<String> testUrl(){
		return ResponseEntity.ok("Hello");
	}
}
