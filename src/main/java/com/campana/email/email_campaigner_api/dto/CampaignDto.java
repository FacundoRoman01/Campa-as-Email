package com.campana.email.email_campaigner_api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CampaignDto {
    private Long id;
    
    @NotBlank(message = "El nombre de la campaña no puede estar vacío")
    @Size(max = 255, message = "El nombre de la campaña no puede exceder los 255 caracteres")
    private String name;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 500, message = "La descripción de la campaña no puede exceder los 500 caracteres")
    private String description;
    
    private String status;
    private LocalDateTime scheduledAt;
    private LocalDateTime sentAt;
    private Long createdById; // Para saber qué usuario la creó
    
    
    //Se agrega el ID de la lista de suscriptores
    @NotNull(message = "Debe especificar un subscriberListId")
    private Long subscriberListId;

    public CampaignDto() {
    }

    public CampaignDto(Long id, String name, String description, String status, LocalDateTime scheduledAt, LocalDateTime sentAt, Long createdById, Long subscriberListId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.scheduledAt = scheduledAt;
        this.sentAt = sentAt;
        this.createdById = createdById;
        this.subscriberListId = subscriberListId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }
    
    public Long getSubscriberListId() {
        return subscriberListId;
    }
    
    public void setSubscriberListId(Long subscriberListId) {
        this.subscriberListId = subscriberListId;
    }
    
}
