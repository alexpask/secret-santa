package org.awsprog.secretsanta.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.mail.Session;

@Data@AllArgsConstructor
public class Email {

    private Session session;

    private String host;

    private String from;
}
