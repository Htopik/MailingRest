package com.example.mailingrest.repository;

import com.example.mailingrest.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Override
    Optional<Post> findById(Long aLong);
    Optional<Post> findByPostcode(Long Index);
    Optional<Post> findByName(String Name);
    Optional<Post> findByAddress(String Address);
}
