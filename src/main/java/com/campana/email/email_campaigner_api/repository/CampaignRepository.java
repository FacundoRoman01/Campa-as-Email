package com.campana.email.email_campaigner_api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campana.email.email_campaigner_api.model.Campaign;
import com.campana.email.email_campaigner_api.model.CampaignStatus;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {
	
	List<Campaign> findByStatusAndScheduledAtBefore(CampaignStatus status, LocalDateTime scheduledAt);
	
}
