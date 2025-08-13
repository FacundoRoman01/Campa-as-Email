package com.campana.email.email_campaigner_api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campana.email.email_campaigner_api.dto.CampaignDto;
import com.campana.email.email_campaigner_api.dto.CampaignScheduleDto;	
import com.campana.email.email_campaigner_api.model.Campaign;
import com.campana.email.email_campaigner_api.model.CampaignStatus;
import com.campana.email.email_campaigner_api.model.SubscriberList;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.CampaignRepository;
import com.campana.email.email_campaigner_api.repository.SubscriberListRepository;
import com.campana.email.email_campaigner_api.repository.UserRepository;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SubscriberListRepository subscriberListRepository;

    /**
     * Crea una nueva campaña para un usuario específico.
     * @param campaignDto Los datos de la campaña a crear.
     * @param createdById El ID del usuario que crea la campaña.
     * @return El DTO de la campaña creada.
     */
    public CampaignDto createCampaign(CampaignDto campaignDto, Long createdById) {
        // Buscar al usuario por su ID
        User createdBy = userRepository.findById(createdById)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
        // Buscar la lista de suscriptores por su ID
        SubscriberList subscriberList = subscriberListRepository.findById(campaignDto.getSubscriberListId())
            .orElseThrow(() -> new RuntimeException("Lista de suscriptores no encontrada"));

        // Convertir el DTO a la entidad Campaign
        Campaign campaign = new Campaign();
        campaign.setName(campaignDto.getName());
        campaign.setDescription(campaignDto.getDescription());
        campaign.setScheduledAt(campaignDto.getScheduledAt());
        campaign.setCreatedBy(createdBy);
        campaign.setStatus(CampaignStatus.DRAFT); // Por defecto, se crea como borrador
        campaign.setSubscriberList(subscriberList); // Asignamos la lista a la campaña

        // Guardar la campaña en la base de datos
        Campaign savedCampaign = campaignRepository.save(campaign);

        // Devolver la entidad convertida a DTO
        return convertToDto(savedCampaign);
    }
    
    /**
     * Obtiene todas las campañas.
     * @return Una lista de DTOs de todas las campañas.
     */
    public List<CampaignDto> getAllCampaigns() {
        return campaignRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Obtiene una campaña por su ID.
     * @param id El ID de la campaña.
     * @return Un Optional que contiene el DTO de la campaña, o vacío si no se encuentra.
     */
    public Optional<CampaignDto> getCampaignById(Long id) {
        return campaignRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * Actualiza una campaña existente.
     * @param id El ID de la campaña a actualizar.
     * @param campaignDto Los nuevos datos de la campaña.
     * @return El DTO de la campaña actualizada.
     */
    public CampaignDto updateCampaign(Long id, CampaignDto campaignDto) {
        return campaignRepository.findById(id)
            .map(campaign -> {
                campaign.setName(campaignDto.getName());
                campaign.setDescription(campaignDto.getDescription());
                campaign.setScheduledAt(campaignDto.getScheduledAt());
                
                // Actualizar la lista de suscriptores si se ha cambiado
                if (!campaign.getSubscriberList().getId().equals(campaignDto.getSubscriberListId())) {
                    SubscriberList newSubscriberList = subscriberListRepository.findById(campaignDto.getSubscriberListId())
                        .orElseThrow(() -> new RuntimeException("Nueva lista de suscriptores no encontrada"));
                    campaign.setSubscriberList(newSubscriberList);
                }
                
                Campaign updatedCampaign = campaignRepository.save(campaign);
                return convertToDto(updatedCampaign);
            })
            .orElseThrow(() -> new RuntimeException("Campaña no encontrada con ID: " + id));
    }
    
    /**
     * Elimina una campaña por su ID.
     * @param id El ID de la campaña a eliminar.
     */
    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }

    /**
     * Nuevo método para programar una campaña.
     * @param campaignId El ID de la campaña a programar.
     * @param scheduleDto El DTO con la fecha y hora de programación.
     * @return El DTO de la campaña actualizada.
     */
    @Transactional
    public CampaignDto scheduleCampaign(Long campaignId, CampaignScheduleDto scheduleDto) {
        return campaignRepository.findById(campaignId)
            .map(campaign -> {
                if (!isAdmin() && !campaign.getCreatedBy().getId().equals(getCurrentUserId())) {
                    throw new RuntimeException("No tiene permisos para programar esta campaña.");
                }

                if (campaign.getStatus() != CampaignStatus.DRAFT) {
                    throw new RuntimeException("Solo se pueden programar campañas en estado DRAFT.");
                }
                if (scheduleDto.getScheduledAt() == null || scheduleDto.getScheduledAt().isBefore(LocalDateTime.now())) {
                    throw new RuntimeException("La fecha de programación no puede ser nula o en el pasado.");
                }

                campaign.setStatus(CampaignStatus.SCHEDULED);
                campaign.setScheduledAt(scheduleDto.getScheduledAt());
                Campaign scheduledCampaign = campaignRepository.save(campaign);
                return convertToDto(scheduledCampaign);
            })
            .orElseThrow(() -> new RuntimeException("Campaña no encontrada con ID: " + campaignId));
    }

    /**
     * Método privado para convertir una entidad Campaign a un CampaignDto.
     * @param campaign La entidad Campaign.
     * @return El DTO de la campaña.
     */
    private CampaignDto convertToDto(Campaign campaign) {
        CampaignDto dto = new CampaignDto();
        dto.setId(campaign.getId());
        dto.setName(campaign.getName());
        dto.setDescription(campaign.getDescription());
        dto.setStatus(campaign.getStatus().name()); // Convertimos el enum a String
        dto.setScheduledAt(campaign.getScheduledAt());
        dto.setSentAt(campaign.getSentAt());
        dto.setId(campaign.getCreatedBy().getId());
        dto.setSubscriberListId(campaign.getSubscriberList().getId()); //Obtenemos el ID de la lista
        return dto;
    }

    /**
     * Obtiene el ID del usuario autenticado a través del contexto de seguridad.
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado en la base de datos."));
            return user.getId();
        }
        throw new RuntimeException("Usuario no autenticado.");
    }
    
    /**
     * Verifica si el usuario autenticado tiene el rol de administrador.
     */
    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
