package com.example.mailingrest.entity;


import com.example.mailingrest.entity.enums.MailState;
import com.example.mailingrest.entity.enums.MailType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
@Table(name="mails")
public class Mail {
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MailType mailtype;

    @Column(nullable = false)
    private long indexRecv;

    @Column(nullable = false)
    private String addressRecv;

    @Column(nullable = false)
    private String nameRecv;

    @Column(columnDefinition = "int default 0")
    @Enumerated(EnumType.STRING)
    private MailState mailState;
}
