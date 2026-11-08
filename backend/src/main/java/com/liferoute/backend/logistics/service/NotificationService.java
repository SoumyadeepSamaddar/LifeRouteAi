package com.liferoute.backend.logistics.service;

public interface NotificationService {
    void send(String channel, String recipient, String subject, String message);
}
