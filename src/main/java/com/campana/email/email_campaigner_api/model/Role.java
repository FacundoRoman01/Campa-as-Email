package com.campana.email.email_campaigner_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "El nombre no puede estar vacio")
	@Column(unique = true, nullable = false)
	private String name;

	public Role() {

	}

	public Role(String name) {
		this.name = name;

	}
	
	
	public Long getId() {
		return id;
	}
	
	
	public String getName() {
		return name;
	}
	
	public void setId(Long id){ 
		this.id = id; 
	}
	
	
	public void setName(String name) {
		this.name = name;
	}

}
