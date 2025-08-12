package com.campana.email.email_campaigner_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.campana.email.email_campaigner_api.model.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long>{

}
