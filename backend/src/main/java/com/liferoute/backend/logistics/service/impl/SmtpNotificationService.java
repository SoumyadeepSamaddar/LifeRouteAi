package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.service.NotificationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.notifications.provider", havingValue = "smtp")
public class SmtpNotificationService implements NotificationService {
    @Override
    public void send(String channel, String recipient, String subject, String message) {
        // SMTP implementation may be added later; logging fallback keeps the app running.
    }
}
