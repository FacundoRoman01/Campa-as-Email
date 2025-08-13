package com.campana.email.email_campaigner_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.dto.SubscriberDto;
import com.campana.email.email_campaigner_api.dto.SubscriberListDto;
import com.campana.email.email_campaigner_api.model.SubscriberList;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.SubscriberListRepository;
import com.campana.email.email_campaigner_api.repository.UserRepository;

@Service
public class SubscriberListService {

	@Autowired
	private SubscriberListRepository subscriberListRepository;

	@Autowired
	private UserRepository userRepository;

	
	// Crea una nueva lista de suscriptores para un usuario.
	public SubscriberListDto createList(SubscriberListDto listDto, Long createdByUserId) {
		// Buscamos al usuario que crea la lista.
		User createdBy = userRepository.findById(createdByUserId)
				.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + createdByUserId));

		// Creamos la entidad SubscriberList a partir del DTO.
		SubscriberList subscriberList = new SubscriberList();
		subscriberList.setName(listDto.getName());
		subscriberList.setCreatedBy(createdBy);

		// Guardamos la lista en la base de datos.
		SubscriberList savedList = subscriberListRepository.save(subscriberList);

		// Convertimos la entidad guardada de nuevo a DTO y la devolvemos.
		return convertToDto(savedList);
	}

	public List<SubscriberListDto> getAllLists() {
		return subscriberListRepository.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	/**
	 * Obtiene una lista de suscriptores por su ID.
	 * 
	 * @param id El ID de la lista.
	 * @return Un Optional con el DTO de la lista, o vacío si no se encuentra.
	 */
	public Optional<SubscriberListDto> getListById(Long id) {
		return subscriberListRepository.findById(id).map(this::convertToDto);
	}

	public SubscriberListDto updateList(Long id, SubscriberListDto updatedListDto) {
		return subscriberListRepository.findById(id).map(list -> {
			list.setName(updatedListDto.getName());
			SubscriberList savedList = subscriberListRepository.save(list);
			return convertToDto(savedList);
		}).orElseThrow(() -> new RuntimeException("Lista de suscriptores no encontrada con ID: " + id));
	}
	
	

	/**
	 * Elimina una lista de suscriptores.
	 * 
	 * @param id El ID de la lista a eliminar.
	 */
	public void deleteList(Long id) {
		subscriberListRepository.deleteById(id);
	}
	
	
	

	/**
	 * Convierte una entidad SubscriberList a su DTO.
	 * 
	 * @param list La entidad a convertir.
	 * @return El DTO.
	 */
	private SubscriberListDto convertToDto(SubscriberList list) {
		SubscriberListDto dto = new SubscriberListDto();
		dto.setId(list.getId());
		dto.setName(list.getName());
		dto.setCreatedByUserId(list.getCreatedBy().getId());

		// Mapeamos los suscriptores de la entidad a DTOs para incluirlos.
		dto.setSubscribers(list.getSubscribers().stream()
				.map(subscriber -> new SubscriberDto(subscriber.getId(), subscriber.getEmail(), list.getId()))
				.collect(Collectors.toSet()));

		return dto;
	}
}
