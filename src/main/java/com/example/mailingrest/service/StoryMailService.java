package com.example.mailingrest.service;


import com.example.mailingrest.entity.Mail;
import com.example.mailingrest.entity.StoryMail;
import com.example.mailingrest.entity.Post;
import com.example.mailingrest.entity.enums.MailState;
import com.example.mailingrest.entity.enums.MailStoryState;
import com.example.mailingrest.repository.MailRepository;
import com.example.mailingrest.repository.StoryMailRepository;
import com.example.mailingrest.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@AllArgsConstructor
public class StoryMailService {
    @Autowired
    StoryMailRepository storyMailRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MailRepository mailRepository;

    public List<StoryMail> getAllMailStories(){
        return storyMailRepository.findAll();
    }
    @Transactional
    public List<Map<String, MailStoryState>> getPostsByUserID(long mail_id){
        List<Map<String, MailStoryState>> res = storyMailRepository.findByMailId(mail_id);
        return res;
    }

    @Transactional
    public String getStatusByMailId(long mail_id){
        if (mailRepository.findById(mail_id).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mail_id + " does not exists")).getMailState() == MailState.PROCESSING){
            return MailState.PROCESSING.toString();
        }
        if (mailRepository.findById(mail_id).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mail_id + " does not exists")).getMailState() == MailState.DELIVERED){
            return MailState.DELIVERED +" in " + mailRepository.findById(mail_id).orElseThrow(() -> new IllegalStateException(
                    "mail with id " + mail_id + " does not exists")).getNameRecv();
        }
        return storyMailRepository.findCurrentStateByMailId(mail_id).orElseThrow(() -> new IllegalStateException(
                "can't departure mail with id " + mail_id + " because it never arrived")) + " " +
                storyMailRepository.findCurrentStopByMailId(mail_id).orElseThrow(() -> new IllegalStateException(
                "can't departure mail with id " + mail_id + " because it never arrived")).getPost().getName();
    }
    @Transactional
    public void addDepartureMailStory(long mailId){
        if(storyMailRepository.findCurrentStateByMailId(mailId).orElseThrow(() -> new IllegalStateException(
                "can't departure mail with id " + mailId + " because it never arrived")) != MailStoryState.ARRIVE){
            throw new IllegalStateException("can't departure mail with id " + mailId + " because it already had departured");
        }
        if(mailRepository.findStateOfMailByMailId(mailId).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mailId + " has no status"))==MailState.DELIVERED) {
            throw new IllegalStateException("can't departure mail with id " + mailId + " because it already had delivered");
        }
        StoryMail storyMail = new StoryMail();
        storyMail.setMail(storyMailRepository.findCurrentStopByMailId(mailId).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + mailId + " does not exists")).getMail());
        storyMail.setPost(storyMailRepository.findCurrentStopByMailId(mailId).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + mailId + " does not exists")).getPost());
        storyMail.setMailStoryState(MailStoryState.DEPARTURE);
        storyMailRepository.save(storyMail);
    }
    @Transactional
    public void addArriveMailStory(long mailId, Post post){
        if(mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + mailId + " does not exists")).getMailState() != MailState.PROCESSING) {
            if (storyMailRepository.findCurrentStateByMailId(mailId).orElse(MailStoryState.ERROR) != MailStoryState.DEPARTURE) {
                throw new IllegalStateException("can't arrive mail with id " + mailId + " because it already had arrived");
            }
        }
        if(mailRepository.findStateOfMailByMailId(mailId).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mailId + " has no status"))==MailState.DELIVERED){
            throw new IllegalStateException("can't arrive mail with id " + mailId + " because it already had delivered");
        }
        StoryMail res = new StoryMail();
        res.setPost(postRepository.findByName(post.getName()).orElseThrow(() -> new IllegalStateException(
                "post with name " + post.getName() + " does not exists")));
        res.setMail(mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + mailId + " does not exists")));
        res.setMailStoryState(MailStoryState.ARRIVE);
        addMailStories(res);
    }
    @Transactional
    public void addDeliveryMailStory(long mailId){
        if(mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + mailId + " does not exists")).getMailState() == MailState.DELIVERED){
            throw new IllegalStateException("can't deliver mail with id " + mailId + " because it already had delivered");
        }
        if (storyMailRepository.findCurrentStateByMailId(mailId).orElse(MailStoryState.ERROR) == MailStoryState.ARRIVE){
            throw new IllegalStateException("can't delivery mail with id " + mailId + " because it hasn't departed yet");
        }
        StoryMail cur = new StoryMail();
        Mail curM = mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                        "mail with id " + mailId + " does not exists"));
        cur.setMail(mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mailId + " does not exists")));
        cur.setPost(postRepository.findByName(mailRepository.findById(mailId).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mailId + " does not exists")).getNameRecv()).orElseThrow(() -> new IllegalStateException(
                "mail with id " + mailId + " does not exists")));
        cur.setMailStoryState(MailStoryState.ARRIVE);
        curM.setMailState(MailState.DELIVERED);
        storyMailRepository.save(cur);
    }
    @Transactional
    public void addMailStories(StoryMail storyMail){
        if (postRepository.findById(storyMail.getPost().getId()).orElseThrow(() -> new IllegalStateException(
                "post with id " + storyMail.getMail().getId() + " does not exists"))
                .getPostcode() == storyMail.getMail().getIndexRecv())
            mailRepository.findById(storyMail.getMail().getId()).orElseThrow(() -> new IllegalStateException(
                    "mailStory with id " + storyMail.getMail().getId() + " does not exists")).setMailState(MailState.DELIVERED);
        else mailRepository.findById(storyMail.getMail().getId()).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + storyMail.getMail().getId() + " does not exists")).setMailState(MailState.MOVEMENT);
        storyMailRepository.save(storyMail);
    }
    public void deleteMailStory(long id){
        storyMailRepository.delete(storyMailRepository.getById(id));
    }
    @Transactional
    public StoryMail updateMailStory(Long id, StoryMail storyMail){
        StoryMail storyMailTemp = this.storyMailRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + id + " does not exists"));
        storyMailTemp.setId(storyMail.getId());
        storyMailTemp.setMail(storyMail.getMail());
        storyMailTemp.setPost(storyMail.getPost());
        storyMailTemp.setMailStoryState(storyMail.getMailStoryState());
        if (storyMail.getPost().equals(postRepository.findByPostcode(storyMail.getMail().getIndexRecv())))
            storyMail.getMail().setMailState(MailState.DELIVERED);
        else storyMail.getMail().setMailState(MailState.MOVEMENT);
        return storyMailTemp;
    }

}
