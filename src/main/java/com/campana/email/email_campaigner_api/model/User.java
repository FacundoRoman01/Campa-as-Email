package com.campana.email.email_campaigner_api.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nombre de usuario no puede estar vacio")
	@Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
	@Column(unique = true, nullable = false)
	private String username;

	@NotBlank(message = "El email no puede estar vacio")
	@Email(message = "Formato de correo electrónico no válido")
	@Column(unique = true, nullable = false)
	private String email;

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
	@Column(nullable = false)
	private String password;
	
	
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id")) 
	private Set<Role> roles = new HashSet<Role>();
	
	
	
	
	public User() {
		
	}
	
	
	
	public User(Long id, String username, String email, String password ) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	
	
	public Long getId() {
		return id;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
    public Set<Role> getRoles(){
    	return roles;
    }
    
    public void setRoles(Set<Role> roles){ 
    	this.roles = roles; 
    }

}
