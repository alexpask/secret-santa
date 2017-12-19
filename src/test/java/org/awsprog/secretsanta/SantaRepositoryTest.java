package org.awsprog.secretsanta;

import org.awsprog.secretsanta.model.Participant;
import org.awsprog.secretsanta.repository.SantaRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SantaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SantaRepository santaRepository;

    @Test
    public void should_check_if_all_emails_have_been_verified() {

        // Given all emails have been verified
        List<Participant> participants = generateParticipants();

        participants.forEach(participant -> {
            participant.setVerified(true);
            entityManager.persist(participant);
        });

        // When the repository is checked for verified
        Optional<List<Participant>> verifiedParticipants = santaRepository.findByVerifiedTrue();
        long unverifiedCount = santaRepository.countByVerifiedFalse();

        // Then all emails have been verified
        assertThat(verifiedParticipants.get().size()).isEqualTo(2);

        // And there are no unverified emails
        assertThat(unverifiedCount).isZero();
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
