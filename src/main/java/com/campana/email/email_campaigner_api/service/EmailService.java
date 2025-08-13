package com.campana.email.email_campaigner_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	
	@Autowired
	private JavaMailSender javaMailSender; //para el envío de correos
	
	
	/**
     * Envía un correo electrónico simple a un suscriptor.
     * En una implementación real, aquí se usaría una plantilla de correo (HTML).
     * @param toEmail El correo electrónico del destinatario.
     * @param subject El asunto del correo.
     * @param body El cuerpo del correo.
     */
    public void sendSimpleEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("facundoromanagustin01@gmail.com"); 
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message); // ENVIAR
    }
	
}
