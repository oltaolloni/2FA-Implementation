package com.example.implementimi2fa;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender {

    private final String host = "smtp.gmail.com";
    private final String port = "587";
    private final String userEmail = "your-email@gmail.com";
    private final String userPassword = "your-password";

    public void sendOtpEmail(String recipientEmail, String otp) throws MessagingException {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userEmail, userPassword);
            }
        });

        Message message = prepareMessage(session, recipientEmail, otp);
        Transport.send(message);
    }

    private Message prepareMessage(Session session, String recipientEmail, String otp) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(userEmail));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is " + otp + ". This code will expire in 5 minutes.");
        return message;
    }
}
