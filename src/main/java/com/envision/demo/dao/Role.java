package com.envision.demo.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String roleType;
	
//	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "roles")
//	private List<User> users = new ArrayList<>();

	public Role(String roleType) {
		this.roleType = roleType;
	}
	
	@Override
	public String toString() {
		return roleType;
	}
}