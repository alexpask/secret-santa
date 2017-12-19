package org.awsprog.secretsanta.service;

import org.awsprog.secretsanta.model.Email;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final Email email;

    public EmailService(final Email email) throws MessagingException {

        this.email = email;
    }

    public Message constructEmail(final String emailAddress) throws MessagingException {

        Message message = new MimeMessage(email.getSession());
        message.setFrom(new InternetAddress(email.getFrom()));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
        message.setSubject("Secret Santa Verification!");
        message.setText("Hello World");

        return message;
    }

    public void sendEmail(final String emailAddress) throws MessagingException {

        Message message = constructEmail(emailAddress);
        Transport.send(message);
    }
}
