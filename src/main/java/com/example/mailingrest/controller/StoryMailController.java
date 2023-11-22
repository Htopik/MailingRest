package com.example.mailingrest.controller;

import com.example.mailingrest.entity.Post;
import com.example.mailingrest.entity.StoryMail;
import com.example.mailingrest.entity.enums.MailStoryState;
import com.example.mailingrest.service.StoryMailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path="api/mailstory")
public class StoryMailController {
    @Autowired
    private StoryMailService storyMailService;

    @GetMapping
    public List<StoryMail> getMailStories(){
        return storyMailService.getAllMailStories();
    }

    @GetMapping("/{mail_id}/story")
    public List<Map<String, MailStoryState>> getMailStoriesByUserID(@PathVariable("mail_id") long mail_id){
        return storyMailService.getPostsByUserID(mail_id);
    }
    @GetMapping("/{mail_id}/status")
    public String getStatusByMailId(@PathVariable("mail_id") long mail_id){
        return storyMailService.getStatusByMailId(mail_id);
    }

    @PostMapping("/{mail_id}/delivery")
    public void addDeliveryMailStory(@PathVariable("mail_id") long mail_id){
        storyMailService.addDeliveryMailStory(mail_id);
    }

    @PostMapping("/{mail_id}/departure")
    public void addDepartureMailStory(@PathVariable("mail_id") long mail_id){
        storyMailService.addDepartureMailStory(mail_id);
    }
    @PostMapping("/{mail_id}/arrive")
    public void addArriveMailStory(@PathVariable("mail_id") long mail_id, @RequestBody @Valid Post post){
        storyMailService.addArriveMailStory(mail_id, post);
    }
    @PostMapping
    public void addMailStories(@RequestBody @Valid StoryMail storyMail){
        storyMailService.addMailStories(storyMail);
    }
    @DeleteMapping("/{id}")
    public void deleteMailStory(@PathVariable("id") Long id){
        storyMailService.deleteMailStory(id);
    }
    @PutMapping("/{id}")
    public StoryMail updateMailStory(@PathVariable("id") Long id, @RequestBody @Valid StoryMail storyMail){
        return storyMailService.updateMailStory(id, storyMail);
    }
}
