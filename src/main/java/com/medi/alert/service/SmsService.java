package com.medi.alert.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final String accountSid = "your_account_sid";
    private final String authToken = "your_auth_token";
    private final String twilioNumber = "your_twilio_number";

    public SmsService() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String to, String body) {
        Message message = Message.creator(new PhoneNumber(to), new PhoneNumber(twilioNumber), body).create();
        System.out.println("Message sent successfully: " + message.getSid());
    }
}