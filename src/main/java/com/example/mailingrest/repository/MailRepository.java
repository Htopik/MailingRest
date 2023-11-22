package com.example.mailingrest.repository;
import com.example.mailingrest.entity.Mail;
import com.example.mailingrest.entity.enums.MailState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MailRepository extends JpaRepository<Mail, Long> {
    @Override
    Optional<Mail> findById(Long aLong);

    @Query(value = "SELECT mails.mail_state FROM mails WHERE mails.id = ?1", nativeQuery = true)
    Optional<MailState> findStateOfMailByMailId(long mail_id);
}
