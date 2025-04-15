package com.MedConnect.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TwilioService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioService.class);

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.whatsappFrom}")
    private String whatsappFrom;  // e.g. whatsapp:+14155238886

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        logger.info("✅ Twilio initialized with SID: {}", accountSid);
    }

    public void sendWhatsAppMessageWithMedia(String toPhoneNumber, String mediaUrl, String caption) {
        try {
            logger.info("Sending WhatsApp media message to: {}", toPhoneNumber);
            logger.info("Media URL: {}", mediaUrl);
            logger.info("Caption: {}", caption);

            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + toPhoneNumber),
                    new PhoneNumber(whatsappFrom),  // From Twilio config
                    caption
            ).setMediaUrl(List.of(URI.create(mediaUrl)))
             .create();

            logger.info("✅ Message sent with SID: {}", message.getSid());
        } catch (ApiException e) {
            logger.error("❌ Failed to send WhatsApp message: {}", e.getMessage());
        }
    }
}
