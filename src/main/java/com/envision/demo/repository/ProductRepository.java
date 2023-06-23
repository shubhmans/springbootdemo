package com.envision.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envision.demo.dao.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

}
