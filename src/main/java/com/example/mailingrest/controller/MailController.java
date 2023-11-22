package com.example.mailingrest.controller;

import com.example.mailingrest.entity.Mail;
import com.example.mailingrest.service.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @GetMapping
    public List<Mail> getMails(){
        return mailService.getAllMails();
    }
    @PostMapping
    public void addMails(@RequestBody @Valid Mail mail){
        mailService.addMails(mail);
    }
    @DeleteMapping("/{id}")
    public void deleteMail(@PathVariable("id") Long id){
        mailService.deleteMail(id);
    }
    @PutMapping("/{id}")
    public Mail updateMail(@PathVariable("id") Long id, @RequestBody @Valid Mail mail){
        return mailService.updateMail(id, mail);
    }
}
