package org.awsprog.secretsanta.config;

import org.awsprog.secretsanta.model.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Session;
import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${santa.email.host}")
    private String host;

    @Value("${santa.email.from}")
    private String from;

    @Bean
    public Email email() {

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.host", host);

        Session session = Session.getDefaultInstance(properties);

        return new Email(session, host, from);
    }
}
