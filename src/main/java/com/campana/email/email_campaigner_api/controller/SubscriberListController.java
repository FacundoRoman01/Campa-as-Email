package com.campana.email.email_campaigner_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campana.email.email_campaigner_api.dto.SubscriberListDto;
import com.campana.email.email_campaigner_api.service.SubscriberListService;
import com.campana.email.email_campaigner_api.repository.UserRepository; //Importamos el repositorio de usuarios

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/subscriber-lists")
public class SubscriberListController {

	@Autowired
	private SubscriberListService subscriberListService;
	
	@Autowired
    private UserRepository userRepository; 

	// Endpoint para crear una nueva lista de suscriptores
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PostMapping
	public ResponseEntity<SubscriberListDto> createList(@RequestBody SubscriberListDto listDto) {

		// ✅ Obtener el ID del usuario autenticado del token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        Long userId = userRepository.findByUsername(username)
        	.orElseThrow(() -> new RuntimeException("Usuario no encontrado")).getId();

		SubscriberListDto newList = subscriberListService.createList(listDto, userId);
		return ResponseEntity.ok(newList);
	}

	// Endpoint para obtener todas las listas (solo para ADMIN)
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<SubscriberListDto>> getAllLists() {
		List<SubscriberListDto> lists = subscriberListService.getAllLists();
		return ResponseEntity.ok(lists);
	}

	// Endpoint para obtener una lista por ID
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
	public ResponseEntity<SubscriberListDto> getListById(@PathVariable Long id) {
		Optional<SubscriberListDto> list = subscriberListService.getListById(id);
		return list.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	// Endpoint para actualizar una lista
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PutMapping("/{id}")
	public ResponseEntity<SubscriberListDto> updateList(@PathVariable Long id,
			@Valid @RequestBody SubscriberListDto listDto) {
		SubscriberListDto updatedList = subscriberListService.updateList(id, listDto);
		return ResponseEntity.ok(updatedList);
	}

	// Endpoint para eliminar una lista (solo para ADMIN)
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteList(@PathVariable Long id) {
		subscriberListService.deleteList(id);
		return ResponseEntity.noContent().build();
	}
}
