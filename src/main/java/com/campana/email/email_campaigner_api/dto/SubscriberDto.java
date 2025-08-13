package com.campana.email.email_campaigner_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SubscriberDto {
	 private Long id;

	    @NotBlank(message = "El email no puede estar vacío")
	    @Email(message = "Debe ser una dirección de email válida")
	    private String email;

	    private Long subscriberListId; // El ID de la lista a la que pertenece
	    
	    // Constructor vacío
	    public SubscriberDto() {
	    }

	    // Constructor completo
	    public SubscriberDto(Long id, String email, Long subscriberListId) {
	        this.id = id;
	        this.email = email;
	        this.subscriberListId = subscriberListId;
	    }
	    
	    // Getters y Setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }
	    
	    public Long getSubscriberListId() {
	        return subscriberListId;
	    }

	    public void setSubscriberListId(Long subscriberListId) {
	        this.subscriberListId = subscriberListId;
	    }
}
