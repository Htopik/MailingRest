package com.example.mailingrest.service;

import com.example.mailingrest.entity.Mail;
import com.example.mailingrest.entity.enums.MailState;
import com.example.mailingrest.repository.MailRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MailService {
    @Autowired
    MailRepository mailRepository;
    public List<Mail> getAllMails(){
        return mailRepository.findAll();
    }
    public void addMails(Mail mail){
        mail.setMailState(MailState.PROCESSING);
        mailRepository.save(mail);
    }
    public void deleteMail(long id){
        mailRepository.delete(mailRepository.getById(id));
    }
    @Transactional
    public Mail updateMail(Long id, Mail mail){
        Mail mailTemp = this.mailRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "mail with id " + id + " does not exists"));
        mailTemp.setId(mail.getId());
        mailTemp.setMailtype(mail.getMailtype());
        mailTemp.setIndexRecv(mail.getIndexRecv());
        mailTemp.setNameRecv(mail.getNameRecv());
        mailTemp.setAddressRecv(mail.getAddressRecv());
        return mailTemp;
    }
}
