// Archivo: SubscriberController.java
package com.campana.email.email_campaigner_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campana.email.email_campaigner_api.dto.SubscriberDto;
import com.campana.email.email_campaigner_api.service.SubscriberService;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {

    @Autowired
    private SubscriberService subscriberService;

    // Endpoint para agregar un suscriptor a una lista específica
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/subscriber-lists/{listId}/subscribers")
    public ResponseEntity<SubscriberDto> addSubscriber(
        @PathVariable Long listId,
        @RequestBody SubscriberDto subscriberDto) {
            SubscriberDto newSubscriber = subscriberService.addSubscriberToList(subscriberDto, listId);
            return ResponseEntity.ok(newSubscriber);
    }
    
    // Endpoint para eliminar un suscriptor por su ID
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/subscribers/{id}")
    public ResponseEntity<Void> removeSubscriber(@PathVariable Long id) {
        subscriberService.removeSubscriber(id);
        return ResponseEntity.noContent().build();
    }
}
