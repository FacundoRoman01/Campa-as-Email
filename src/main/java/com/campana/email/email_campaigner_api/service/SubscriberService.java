package com.campana.email.email_campaigner_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.dto.SubscriberDto;
import com.campana.email.email_campaigner_api.model.Subscriber;
import com.campana.email.email_campaigner_api.model.SubscriberList;
import com.campana.email.email_campaigner_api.repository.SubscriberListRepository;
import com.campana.email.email_campaigner_api.repository.SubscriberRepository;

@Service
public class SubscriberService {
	@Autowired
    private SubscriberRepository subscriberRepository;

    @Autowired
    private SubscriberListRepository subscriberListRepository;
	
    //añadir Suscriptor a La ista
    public SubscriberDto addSubscriberToList(SubscriberDto subscriberDto, Long subscriberListId) {
        // Buscamos la lista de suscriptores.
        SubscriberList list = subscriberListRepository.findById(subscriberListId)
            .orElseThrow(() -> new RuntimeException("Lista de suscriptores no encontrada"));
        
        // Creamos la entidad Subscriber.
        Subscriber subscriber = new Subscriber();
        subscriber.setEmail(subscriberDto.getEmail());
        subscriber.setList(list);
        
        // Guardamos el suscriptor.
        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        
        // Convertimos la entidad guardada de nuevo a DTO y la devolvemos.
        return new SubscriberDto(savedSubscriber.getId(), savedSubscriber.getEmail(), list.getId());
    }
    
    /**
     * Elimina un suscriptor de una lista.
     * @param id El ID del suscriptor a eliminar.
     */
    public void removeSubscriber(Long id) {
        subscriberRepository.deleteById(id);
    }	
}
