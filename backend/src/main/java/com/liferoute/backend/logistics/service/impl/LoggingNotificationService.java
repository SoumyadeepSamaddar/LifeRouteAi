package com.liferoute.backend.logistics.service.impl;

import com.liferoute.backend.logistics.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Primary
public class LoggingNotificationService implements NotificationService {
    @Override
    public void send(String channel, String recipient, String subject, String message) {
        log.info("{} notification to {} [{}]: {}", channel, recipient, subject, message);
    }
}
