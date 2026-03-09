package com.joshi.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    @Value("${twilio.accountSid}")
    private String accountSid;

    @Value("${twilio.authToken}")
    private String authToken;

    @Value("${twilio.from}")
    private String from;

    @Value("${twilio.to}")
    private String to;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendWhatsAppMessage(String messageBody) {
        Message message = Message.creator(
                new PhoneNumber(to),
                new PhoneNumber(from),
                messageBody
        ).create();
    }
}
