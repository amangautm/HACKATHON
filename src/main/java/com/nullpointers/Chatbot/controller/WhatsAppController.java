package com.nullpointers.Chatbot.controller;

import com.nullpointers.Chatbot.service.TwilioService;
import com.nullpointers.Chatbot.service.UserInteractionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {

    @Autowired
    private UserInteractionService userInteractionService;

    @Autowired
    private TwilioService twilioService;

    @Value("${server.url}")
    private String serverUrl;


    @PostMapping("/callback")
    public ResponseEntity<Void> receiveMessage(HttpServletRequest request, @RequestParam("From") String from, @RequestParam("Body") String body) {
        String requestUrl = serverUrl + request.getRequestURI();
        userInteractionService.handleIncomingMessage(from, body);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/sendIntro")
    public String sendIntroMessage(@RequestParam String to) {
            twilioService.sendInteractiveMessage(to);
        return "Introductory interactive message sent.";
    }

    @PostMapping("/sendJoinCodeAndIntro")
    public String sendJoinCodeAndIntroMessage(@RequestParam String to) {
        twilioService.sendJoinInstructions(to);
        return "Join code and introductory message sent successfully!";
    }
}
