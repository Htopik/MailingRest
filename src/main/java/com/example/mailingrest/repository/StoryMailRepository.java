package com.example.mailingrest.repository;

import com.example.mailingrest.entity.StoryMail;
import com.example.mailingrest.entity.enums.MailStoryState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StoryMailRepository extends JpaRepository<StoryMail, Long> {
    @Override
    Optional<StoryMail> findById(Long along);
    @Query(value = "SELECT posts.name, storiesmail.mail_story_state FROM storiesmail inner join posts WHERE storiesmail.mail_id = ?1 and posts.id = storiesmail.post_id",
            nativeQuery = true)
    List<Map<String, MailStoryState>> findByMailId(long mail_id);

    @Query(value = "SELECT storiesmail.mail_story_state FROM storiesmail WHERE storiesmail.mail_id = ?1 and storiesmail.id = (SELECT max(storiesmail.id) FROM storiesmail " +
            "WHERE storiesmail.mail_id = ?1)", nativeQuery = true)
    Optional<MailStoryState> findCurrentStateByMailId(long mail_id);

    @Query(value = "SELECT * FROM storiesmail WHERE storiesmail.mail_id = ?1 and storiesmail.id = (SELECT max(storiesmail.id) FROM storiesmail WHERE storiesmail.mail_id = ?1)",
            nativeQuery = true)
    Optional<StoryMail> findCurrentStopByMailId(long mail_id);


}
