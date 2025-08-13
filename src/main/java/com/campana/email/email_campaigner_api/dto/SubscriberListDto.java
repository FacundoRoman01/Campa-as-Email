package com.campana.email.email_campaigner_api.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SubscriberListDto {
	
	private Long id;

    @NotBlank(message = "El nombre de la lista no puede estar vacío")
    @Size(max = 255, message = "El nombre de la lista no puede exceder los 255 caracteres")
    private String name;

    private Long createdByUserId; // El ID del usuario que creó la lista
    
    private Set<SubscriberDto> subscribers; // Para incluir a los suscriptores en la respuesta
    
    // Constructor vacío
    public SubscriberListDto() {
    }

    // Constructor completo
    public SubscriberListDto(Long id, String name, Long createdByUserId, Set<SubscriberDto> subscribers) {
        this.id = id;
        this.name = name;
        this.createdByUserId = createdByUserId;
        this.subscribers = subscribers;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Long createdByUserId) {
        this.createdByUserId = createdByUserId;
    }
    
    public Set<SubscriberDto> getSubscribers() {
        return subscribers;
    }
    
    public void setSubscribers(Set<SubscriberDto> subscribers) {
        this.subscribers = subscribers;
    }
}
