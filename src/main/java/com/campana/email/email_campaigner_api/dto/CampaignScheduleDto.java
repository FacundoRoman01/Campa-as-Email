package com.campana.email.email_campaigner_api.dto;

import java.time.LocalDateTime;

public class CampaignScheduleDto {

    private LocalDateTime scheduledAt;

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }
}
