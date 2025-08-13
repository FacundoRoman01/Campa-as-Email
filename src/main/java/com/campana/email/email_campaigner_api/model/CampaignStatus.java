package com.campana.email.email_campaigner_api.model;

public enum CampaignStatus {
	DRAFT, // En borrador, no está lista para ser enviada
	SCHEDULED, // Programada para ser enviada en el futuro
	 SENDING,    // En proceso de envío.
	SENT, // Ya ha sido enviada
	FAILED      // Fallo en el envío.
}
