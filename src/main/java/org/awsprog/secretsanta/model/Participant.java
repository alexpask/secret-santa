package org.awsprog.secretsanta.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Participant {

    public Participant() {}

    public Participant(final String name, final String email) {
        this.name = name;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;
    private boolean emailSent;
    private boolean verified;
    private String guid;
}
