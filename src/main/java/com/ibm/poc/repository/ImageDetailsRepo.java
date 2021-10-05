package com.ibm.poc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ibm.poc.entity.ImageDetails;

public interface ImageDetailsRepo extends JpaRepository<ImageDetails, Integer> {

}
