package org.awsprog.secretsanta.service;

import lombok.extern.slf4j.Slf4j;
import org.awsprog.secretsanta.repository.SantaRepository;
import org.awsprog.secretsanta.model.Participant;
import org.hibernate.annotations.NotFound;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class SantaService {

    private final EmailService emailService;
    private final SantaRepository santaRepository;

    public SantaService(final EmailService emailService,
                        final SantaRepository santaRepository) {

        this.emailService = emailService;
        this.santaRepository = santaRepository;
    }

    public void processNew(List<Participant> participants) {

        participants.stream().forEach(participant -> {
            try {
                emailService.sendEmail(participant.getEmail());
                participant.setEmailSent(true);
                participant.setGuid(UUID.randomUUID().toString());
                santaRepository.save(participant);
            } catch (MessagingException e) {
                e.printStackTrace();
               participant.setEmailSent(false);
            }
        });

    }

    public Participant verifyParticipant(String guid) {

        Participant participant = santaRepository.findByGuid(guid);

        if (participant != null) {
            log.info("Found participant with guid: {0}", participant.getGuid());
            participant.setVerified(true);
            santaRepository.save(participant);
        }

        return participant;
    }

    public Optional<List<Participant>> findVerified() {

        // First check if there are any unverified email remaining
        long numberUnverified = santaRepository.countByVerifiedFalse();

        if (numberUnverified > 0) { return Optional.empty(); }

        // Second check count of verified emails is not zero
        Optional<List<Participant>> verifiedParticipants = santaRepository.findByVerifiedTrue();

        if (verifiedParticipants.isPresent() &&
                verifiedParticipants.get().size() != 0) {

            return verifiedParticipants;
        }

        return Optional.empty();
    }

    @Scheduled()
    public void checkIfEveryoneHasResponded() {

    }
}
