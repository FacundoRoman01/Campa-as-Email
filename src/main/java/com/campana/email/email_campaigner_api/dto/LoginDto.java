package com.campana.email.email_campaigner_api.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {
	
	
	@NotBlank(message = "El nombre de usuario o email no puede estar vacío")
	private String usernameOrEmail;
	
	@NotBlank(message = "La contraseña no puede estar vacía")
	private String password;
	
	
	
	
	public LoginDto(String usernameOrEmail, String password) {
		this.usernameOrEmail = usernameOrEmail;
		this.password = password;
	}
	
	
	
	
	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	
	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}
	
	

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
