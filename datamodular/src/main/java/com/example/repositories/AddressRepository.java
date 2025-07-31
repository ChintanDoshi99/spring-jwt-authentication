package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.datamodular.entities.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
}
