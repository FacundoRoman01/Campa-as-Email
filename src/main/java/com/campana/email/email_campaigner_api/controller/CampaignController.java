package com.campana.email.email_campaigner_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication; // ✅ Importamos la clase de autenticación
import org.springframework.security.core.context.SecurityContextHolder; // ✅ Importamos el contexto de seguridad
import org.springframework.security.core.userdetails.UserDetails; // ✅ Importamos la interfaz UserDetails
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.campana.email.email_campaigner_api.dto.CampaignDto;
import com.campana.email.email_campaigner_api.dto.CampaignScheduleDto;
import com.campana.email.email_campaigner_api.service.CampaignService;
import com.campana.email.email_campaigner_api.repository.UserRepository; // ✅ Importamos el repositorio de usuarios

@RestController 
@RequestMapping("/api/v1/campaigns") 
public class CampaignController {
    
    @Autowired
    private CampaignService campaignService;
    
    @Autowired
    private UserRepository userRepository; // ✅ Inyectamos el repositorio de usuarios

    
    // @PreAuthorize Solo deja pasar a usuarios con rol 'ADMIN' o 'USER'.
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CampaignDto> createCampaign(@RequestBody CampaignDto campaignDto) {
        // ✅ Obtener el ID del usuario autenticado desde el token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();
        
        Long userId = userRepository.findByUsername(username)
        	.orElseThrow(() -> new RuntimeException("Usuario no encontrado")).getId();

        // Le pasamos el pedido (el servicio) para que cree la campaña.
        CampaignDto newCampaign = campaignService.createCampaign(campaignDto, userId);

        // Le devolvemos al cliente una respuesta exitosa (código 201 OK) con la nueva campaña creada.
        return ResponseEntity.ok(newCampaign);
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CampaignDto>> getAllCampaigns() {
        // Le pedimos que nos dé todas las campañas.
        List<CampaignDto> campaigns = campaignService.getAllCampaigns();
        
        // Le devolvemos al cliente la lista de campañas.
        return ResponseEntity.ok(campaigns);
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDto> getCampaignById(@PathVariable Long id) {
        // Le pedimos que busque la campaña por su ID.
        Optional<CampaignDto> campaign = campaignService.getCampaignById(id);
        
        // Si la encuentra, la devuelve. Si no, devuelve un error 404 (Not Found).
        return campaign.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CampaignDto> updateCampaign(@PathVariable Long id, @RequestBody CampaignDto campaignDto) {
        // Le pedimos que actualice la campaña con los nuevos datos.
        CampaignDto updatedCampaign = campaignService.updateCampaign(id, campaignDto);
        return ResponseEntity.ok(updatedCampaign);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        // Le pedimos que elimine la campaña.
        campaignService.deleteCampaign(id);
        
        // Devolvemos una respuesta exitosa sin contenido (código 204 No Content).
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Nuevo endpoint para programar una campaña.
     * Solo accesible para usuarios con rol 'ADMIN'.
     */
    @PostMapping("/{id}/schedule")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CampaignDto> scheduleCampaign(@PathVariable Long id, @RequestBody CampaignScheduleDto scheduleDto) {
        return ResponseEntity.ok(campaignService.scheduleCampaign(id, scheduleDto));
    }
    
}
