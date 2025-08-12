package com.campana.email.email_campaigner_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campana.email.email_campaigner_api.model.Campaign;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

}
