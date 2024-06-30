package com.nullpointers.Chatbot.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.SneakyThrows;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

@Service
public class TwilioService {
    @Value("${twilio.whatsapp.number}")
    private String fromNumber;

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.sandbox.join.code}")
    private String sandboxJoinCode;

    @Value("${twilio.sms.number}")
    private String smsNumber;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Twilio.init(accountSid, authToken);
    }

    public void sendMessage(String to, String message) {
        Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(fromNumber),
                message
        ).create();
    }

    public void sendInteractiveMessage(String to) {
        String messageBody = "Hello! Welcome to our booking service. Please select an option:\n" +
                "1. Booking\n" +
                "2. Track";

        Message message = Message.creator(
                        new PhoneNumber("whatsapp:+" + to),
                        new PhoneNumber(fromNumber),
                        messageBody)
                .create();
    }

//    @SneakyThrows
//    public void sendInteractiveMessage(String to) throws IOException {
//        String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
//        JSONObject payload = new JSONObject();
//        payload.put("To", "whatsapp:+" + to);
//        payload.put("From", fromNumber);
//
//        JSONObject interactive = new JSONObject();
//        interactive.put("type", "button");
//
//        JSONArray actionButtons = new JSONArray();
//
//        JSONObject bookButton = new JSONObject();
//        bookButton.put("type", "reply");
//        bookButton.put("reply", new JSONObject().put("id", "book").put("title", "Booking"));
//        actionButtons.put(bookButton);
//
//        JSONObject trackButton = new JSONObject();
//        trackButton.put("type", "reply");
//        trackButton.put("reply", new JSONObject().put("id", "track").put("title", "Track"));
//        actionButtons.put(trackButton);
//
//        interactive.put("action", new JSONObject().put("buttons", actionButtons));
//        payload.put("interactive", interactive);
//
//        try (CloseableHttpClient client = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost(url);
//            request.addHeader("Authorization", "Basic " + Base64.getEncoder().encodeToString((accountSid + ":" + authToken).getBytes()));
//            request.addHeader("Content-Type", "application/json");
//            request.setEntity(new StringEntity(payload.toString()));
//            System.out.println(payload.toString());
//
//            try (CloseableHttpResponse response = client.execute(request)) {
//                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//                StringBuilder responseString = new StringBuilder();
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    responseString.append(line);
//                }
//                System.out.println("Twilio Response: " + responseString.toString());
//
//                int statusCode = response.getStatusLine().getStatusCode();
//                if (statusCode == 200) {
//                    System.out.println("Message sent successfully.");
//                } else {
//                    System.out.println("Failed to send message. Status code: " + statusCode);
//                }
//            }
//        }
//    }

    public void sendJoinInstructions(String to) {
        try {
            String encodedJoinCode = URLEncoder.encode(sandboxJoinCode, "UTF-8");
            String whatsappJoinLink = "https://wa.me/" + fromNumber.replace("whatsapp:", "") + "?text=" + encodedJoinCode;
            String messageBody = "Welcome to DP WORLD booking services. " +
                    "Please click the link to join our WhatsApp services and start booking and tracking your containers: " +
                    whatsappJoinLink;

            Message message = Message.creator(
                    new PhoneNumber("+" + to),
                    new PhoneNumber(smsNumber),
                    messageBody).create();
            System.out.println(message.getSid());
        } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }
    }
}
