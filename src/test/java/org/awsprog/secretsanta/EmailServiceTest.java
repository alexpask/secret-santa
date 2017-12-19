package org.awsprog.secretsanta;

import org.awsprog.secretsanta.model.Email;
import org.awsprog.secretsanta.service.EmailService;
import org.junit.Test;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmailServiceTest {

    @Test
    public void should_configure_email() throws MessagingException {

        // Given
        Email email = mock(Email.class);
        when(email.getHost()).thenReturn("localhost");
        when(email.getFrom()).thenReturn("test@email.com");
        EmailService emailService = new EmailService(email);

        // When
        Message message = emailService.constructEmail("pask200@gmail.com");

        // Then
        assertThat(message.getSubject())
                .isNotEmpty();
    }
}
