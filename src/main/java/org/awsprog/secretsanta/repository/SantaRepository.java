package org.awsprog.secretsanta.repository;

import org.awsprog.secretsanta.model.Participant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SantaRepository extends CrudRepository<Participant, Long> {

    Participant findByGuid(String guid);

    long countByVerifiedFalse();

    Optional<List<Participant>> findByVerifiedTrue();
}
