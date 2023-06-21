package com.envision.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.envision.demo.dao.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	
}
