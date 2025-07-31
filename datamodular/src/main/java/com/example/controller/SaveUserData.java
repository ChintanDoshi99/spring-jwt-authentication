package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.datamodular.entities.EntityDetails;
import com.example.repositories.EntityRepository;

@RestController
@RequestMapping("/addUserData")
public class SaveUserData {
	@Autowired
	private EntityRepository userRepository;
	
	@PostMapping
	public ResponseEntity<String> createUser(@RequestBody EntityDetails user){
		userRepository.save(user);
		return ResponseEntity.ok("User Details and address saved");
	}
}
