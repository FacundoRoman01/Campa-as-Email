package com.campana.email.email_campaigner_api.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.campana.email.email_campaigner_api.dto.CampaignDto;
import com.campana.email.email_campaigner_api.model.Campaign;
import com.campana.email.email_campaigner_api.model.CampaignStatus;
import com.campana.email.email_campaigner_api.model.User;
import com.campana.email.email_campaigner_api.repository.CampaignRepository;
import com.campana.email.email_campaigner_api.repository.UserRepository;

@Service
public class CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private UserRepository userRepository;

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

        // Convertir el DTO a la entidad Campaign
        Campaign campaign = new Campaign();
        campaign.setName(campaignDto.getName());
        campaign.setDescription(campaignDto.getDescription());
        campaign.setScheduledAt(campaignDto.getScheduledAt());
        campaign.setCreatedBy(createdBy);
        campaign.setStatus(CampaignStatus.DRAFT); // Por defecto, se crea como borrador

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
     * @param El ID de la campaña.
     * @return Un Optional que contiene el DTO de la campaña, o vacío si no se encuentra.
     */
    public Optional<CampaignDto> getCampaignById(Long id) {
        return campaignRepository.findById(id).map(this::convertToDto);
    }
    
    /**
     * Actualiza una campaña existente.
     * @param  El ID de la campaña a actualizar.
     * @param campaignDto Los nuevos datos de la campaña.
     * @return El DTO de la campaña actualizada.
     */
    public CampaignDto updateCampaign(Long id, CampaignDto campaignDto) {
        return campaignRepository.findById(id)
            .map(campaign -> {
                campaign.setName(campaignDto.getName());
                campaign.setDescription(campaignDto.getDescription());
                campaign.setScheduledAt(campaignDto.getScheduledAt());
                
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
     * Método privado para convertir una entidad Campaign a un CampaignDto.
     * @param  La entidad Campaign.
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
        dto.setCreatedById(campaign.getCreatedBy().getId());
        return dto;
    }
}
