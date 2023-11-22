package com.example.mailingrest.service;

import com.example.mailingrest.entity.Post;
import com.example.mailingrest.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PostService {
    @Autowired
    PostRepository postRepository;
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }
    @Transactional
    public void addPosts(Post post){
        if(postRepository.findByPostcode(post.getPostcode()).isPresent()) throw new IllegalStateException("index taken");
        else if (postRepository.findByName(post.getName()).isPresent()) throw new IllegalStateException("name taken");
        else if (postRepository.findByAddress(post.getAddress()).isPresent()) throw new IllegalStateException("address taken");
        else postRepository.save(post);
    }
    public void deletePost(long id){
        postRepository.delete(postRepository.getById(id));
    }
    @Transactional
    public Post updatePost(Long id, Post post){
        Post postTemp = this.postRepository.findById(id).orElseThrow(() -> new IllegalStateException(
                "post with id " + id + " does not exists"));
        if(postRepository.findByPostcode(post.getPostcode()).isPresent() && postRepository.findByPostcode(post.getPostcode()).
                orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + id + " does not exists")).getId()!=post.getId()) throw new IllegalStateException("index taken");
        else if (postRepository.findByName(post.getName()).isPresent() && postRepository.findByPostcode(post.getPostcode()).
                orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + id + " does not exists")).getId()!=post.getId()) throw new IllegalStateException("name taken");
        else if (postRepository.findByAddress(post.getAddress()).isPresent() && postRepository.findByPostcode(post.getPostcode()).
                orElseThrow(() -> new IllegalStateException(
                "mailStory with id " + id + " does not exists")).getId()!=post.getId()) throw new IllegalStateException("address taken");
        else{
            postTemp.setPostcode(post.getPostcode());
            postTemp.setName(post.getName());
            postTemp.setAddress(post.getAddress());
        }
        return postTemp;
    }
}
