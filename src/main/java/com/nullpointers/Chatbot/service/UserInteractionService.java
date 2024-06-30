package com.nullpointers.Chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInteractionService {

    @Autowired
    private TwilioService whatsAppService;

    @Autowired
    private BookingService bookingService;

    public void handleIncomingMessage(String from, String body) {
        if (body.toLowerCase().contains("booking")) {
            createBooking(from);
        } else if (body.toLowerCase().contains("track")) {
            trackBooking(from);
        } else {
            sendMessage(from, "I'm sorry, I didn't understand that. You can say 'create booking' or 'track booking'.");
        }
    }

    private void createBooking(String from) {
        String bookingId = bookingService.createBooking(from);
        sendMessage(from, "Your booking has been created with ID: " + bookingId);
    }

    private void trackBooking(String from) {
        String status = bookingService.trackBooking(from);
        sendMessage(from, "Your tracking status is: " + status);
    }

    private void sendMessage(String to, String message) {
        whatsAppService.sendMessage(to, message);
    }
}
