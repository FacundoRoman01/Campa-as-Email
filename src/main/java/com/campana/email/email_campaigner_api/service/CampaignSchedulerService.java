package com.campana.email.email_campaigner_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.campana.email.email_campaigner_api.model.Campaign;
import com.campana.email.email_campaigner_api.model.CampaignStatus;
import com.campana.email.email_campaigner_api.model.Subscriber;
import com.campana.email.email_campaigner_api.repository.CampaignRepository;

@Service
public class CampaignSchedulerService {

    @Autowired
    private CampaignRepository campaignRepository;
    
    @Autowired
    private EmailService emailService;

    // La anotación @Scheduled configura un trabajo programado que se ejecuta cada 30 segundos.
    // Esto es solo un ejemplo; se puede ajustar según la necesidad.
    @Scheduled(fixedRate = 30000)
    @Transactional
    public void sendScheduledCampaigns() {
        // Buscar todas las campañas que estén programadas y cuya hora de envío ya haya pasado.
        List<Campaign> campaignsToSend = campaignRepository.findByStatusAndScheduledAtBefore(
            CampaignStatus.SCHEDULED, LocalDateTime.now()
        );

        // Iterar sobre las campañas encontradas para procesar cada una.
        for (Campaign campaign : campaignsToSend) {
            try {
                // 1. Actualizar el estado de la campaña a ENVIANDO.
                campaign.setStatus(CampaignStatus.SENDING);
                campaignRepository.save(campaign);
                
                // 2. Enviar el correo a cada suscriptor de la lista asociada.
                for (Subscriber subscriber : campaign.getSubscriberList().getSubscribers()) {
                    String subject = "Campaña: " + campaign.getName();
                    String body = campaign.getDescription();
                    emailService.sendSimpleEmail(subscriber.getEmail(), subject, body);
                }
                
                // 3. Actualizar el estado a ENVIADO y registrar la hora.
                campaign.setStatus(CampaignStatus.SENT);
                campaign.setSentAt(LocalDateTime.now());
                campaignRepository.save(campaign);

            } catch (Exception e) {
                // 4. Si ocurre un error, actualizar el estado a FALLIDO.
                campaign.setStatus(CampaignStatus.FAILED);
                campaignRepository.save(campaign);
                System.err.println("Error al enviar la campaña con ID: " + campaign.getId() + ". Error: " + e.getMessage());
                // Imprimir el stack trace para depuración.
                e.printStackTrace();
            }
        }
    }
}
