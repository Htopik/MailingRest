package com.example.mailingrest.controller;


import com.example.mailingrest.entity.Post;
import com.example.mailingrest.service.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path="api/post")
public class PostController {
    @Autowired
    private PostService postService;

    @GetMapping
    public List<Post> getPosts(){
        return postService.getAllPosts();
    }
    @PostMapping
    public void addPosts(@RequestBody @Valid Post post){
        postService.addPosts(post);
    }
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);
    }
    @PutMapping("/{id}")
    public Post updatePost(@PathVariable("id") Long id, @RequestBody @Valid Post post){
        return postService.updatePost(id, post);
    }
}
