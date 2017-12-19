package org.awsprog.secretsanta.controller;

import lombok.extern.slf4j.Slf4j;
import org.awsprog.secretsanta.model.Participant;
import org.awsprog.secretsanta.service.SantaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@Slf4j
public class SantaController {

    private final SantaService santaService;

    public SantaController(final SantaService santaService) {

        this.santaService = santaService;
    }

    @PostMapping(path = "/api/participants",
                 consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void addParticipants(@RequestBody List<Participant> participants) {

        santaService.processNew(participants);
    }

    @GetMapping(path = "/api/participants/verify/{guid}")
    @ResponseStatus(HttpStatus.OK)
    public void verifyParticipant(@PathVariable(value = "guid") String guid) {

        log.info("Verifying: " + guid);
        santaService.verifyParticipant(guid);
    }
}
