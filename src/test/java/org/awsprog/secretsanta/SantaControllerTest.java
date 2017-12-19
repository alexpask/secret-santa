package org.awsprog.secretsanta;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.awsprog.secretsanta.controller.SantaController;
import org.awsprog.secretsanta.model.Participant;
import org.awsprog.secretsanta.service.SantaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SantaControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper OBJECT = new ObjectMapper();

    @Mock
    private SantaService santaService;

    @InjectMocks
    private SantaController santaController;

    @Before
    public void setup() {

        mockMvc = MockMvcBuilders.standaloneSetup(santaController).build();
    }

	@Test
	public void should_accept_list_of_participants() throws Exception {

		// Given a list of particpants
        Participant one = new Participant("one", "one@gmail.com");
        Participant two = new Participant("two", "two@gmail.com");
        List<Participant> participants = new ArrayList<>();
        participants.add(one);
        participants.add(two);

        // When endpoint is called
        mockMvc.perform(post("/api/participants")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(OBJECT.writeValueAsString(participants)))

        // Then 201 is returned
                .andExpect(status().isCreated());

        // And
        verify(santaService, times(1))
                .processNew(participants);
	}


    @Test
    public void should_accept_verified_email() throws Exception {

        // Given I click on a link in an email
        String guid = UUID.randomUUID().toString();
        String url = String.format("/api/participants/verify/%s", guid);

        // When the verify endpoint is called
        mockMvc.perform(get(url))

        // Then Ok is returned
                .andExpect(status().isOk());

        // And santa service is verified
        verify(santaService, times(1)).verifyParticipant(guid);
    }
}
