package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.datamodular.entities.*;

public interface EntityRepository extends JpaRepository<EntityDetails, Long> {

}
