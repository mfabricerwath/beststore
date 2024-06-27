package com.mugisha.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mugisha.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
