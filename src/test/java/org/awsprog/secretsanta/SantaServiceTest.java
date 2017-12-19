package org.awsprog.secretsanta;

import org.awsprog.secretsanta.model.Participant;
import org.awsprog.secretsanta.repository.SantaRepository;
import org.awsprog.secretsanta.service.EmailService;
import org.awsprog.secretsanta.service.SantaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class SantaServiceTest {

    @Mock
    private SantaRepository repository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private SantaService santaService;

    @Test
    public void should_Save_Participants() {

        // Given a list of participants
       List<Participant> participants = generateParticipants();

        // When participants are saved
        santaService.processNew(participants);

        // Then the number saved is equal to the number of participants
        verify(repository, times(participants.size())).save(any(Participant.class));
    }

    @Test
    public void should_Send_Emails() throws MessagingException {

        // Given a list of participants to email
        List<Participant> participants = generateParticipants();

        // When new participants are processed
        santaService.processNew(participants);

        // Then the email service is called the correct amount of times
        verify(emailService, times(participants.size())).sendEmail(anyString());
    }

    @Test
    public void should_mark_verified_email() {

        // Given someone has clicked on an verification email
        String guid = UUID.randomUUID().toString();

        when(repository.findByGuid(guid))
                .thenReturn(new Participant("one", "one@gmail.com"));

        // When SantaService handles the verification
        Participant participant = santaService.verifyParticipant(guid);

        // Then participant is marked as verified
        verify(repository, times(1))
                .findByGuid(guid);
        verify(repository, times(1))
                .save(any(Participant.class));

        // And participant's email is marked as verified
        assertThat(participant.isVerified())
                .isTrue();
    }

    @Test
    public void should_check_all_emails_are_verified() {

        // Given all emails have been verified
        when(repository.countByVerifiedFalse())
                .thenReturn(0L);
        when(repository.findByVerifiedTrue())
                .thenReturn(Optional.of(generateParticipants()));

        // When check for all emails are verified
        Optional<List<Participant>> isTimeToMakePick = santaService.findVerified();

        // Then its time to make santa selections
        assertThat(isTimeToMakePick.isPresent()).isTrue();
    }

    private List<Participant> generateParticipants() {
        Participant one = new Participant("one", "one@gmail.com");
        Participant two = new Participant("two", "two@gmail.com");
        List<Participant> participants = new ArrayList<>();
        participants.add(one);
        participants.add(two);

        return participants;
    }
}
