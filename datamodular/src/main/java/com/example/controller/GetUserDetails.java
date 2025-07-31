package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.datamodular.entities.EntityDetails;
import com.example.repositories.EntityRepository;

@RestController
@RequestMapping("/getUser")
public class GetUserDetails {
	
	@Autowired
	private EntityRepository userRepository;

	@GetMapping("/{id}")
	public ResponseEntity<EntityDetails> getUserDetails(@PathVariable Long id){
		EntityDetails user= getUserWithAddress(id);
		return ResponseEntity.ok(user);
	}

	private EntityDetails getUserWithAddress(Long id) {
		return userRepository.findById(id).orElseThrow(()-> new RuntimeException("No user found"));
		
	} 
}
