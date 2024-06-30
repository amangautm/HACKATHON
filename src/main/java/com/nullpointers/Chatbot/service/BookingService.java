package com.nullpointers.Chatbot.service;

import org.springframework.stereotype.Service;

@Service
public class BookingService {

    public String createBooking(String user) {
       System.out.println("************************ CREATE BOOKING ********************************");
       return "aMan"+Math.random();
    }

    public String trackBooking(String user) {
        System.out.println("************************ TRACKING ********************************");
        return "TRACK"+Math.random();
    }
}
