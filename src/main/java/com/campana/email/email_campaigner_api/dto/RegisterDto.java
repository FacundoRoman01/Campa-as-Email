package com.campana.email.email_campaigner_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterDto {

	@NotBlank(message = "Nombre de usuario no puede estar vacio")
	@Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
	private String username;

	@NotBlank(message = "El email no puede estar vacio")
	@Email(message = "Formato de correo electrónico no válido")
	private String email;

	@NotBlank(message = "La contraseña no puede estar vacía")
	@Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
	private String password;
	
	@NotBlank(message = "El role no puede estar vacio")
	private String role;
	
	
	public RegisterDto() {
		
	}
	
	
	public RegisterDto(String username, String email, String password, String role) {

		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
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
	
	 public String getRole(){ 
		 return role; 
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
	
	public void setRole(String role){
	    this.role = role; 
	}
	

}
