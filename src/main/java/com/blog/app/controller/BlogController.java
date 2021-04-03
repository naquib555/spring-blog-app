package com.blog.app.controller;

import com.blog.app.common.enums.Flag;
import com.blog.app.enums.Rating;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.blog.BlogDetailsResponse;
import com.blog.app.model.payload.blog.CommentRequest;
import com.blog.app.model.payload.blog.PostBlogRequest;
import com.blog.app.security.domain.UserPrincipal;
import com.blog.app.service.IBlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("blog")
public class BlogController {

    @Autowired
    private IBlogService blogService;

    @PostMapping("/post")
    public ApiResponse postBlog(@Valid @RequestBody PostBlogRequest postBlogRequest, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.postBlog(postBlogRequest, userPrincipal.getUserId());
    }

    @GetMapping("/getBlogPost/{id}")
    public ApiResponse<BlogDetailsResponse> getBlog(@PathVariable Long id) {
        return blogService.getBlog(id);
    }

    @GetMapping("/approvalRequest/{id}")
    public ApiResponse requestForApproval(@PathVariable Long id, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.sendForApproval(id, userPrincipal.getUserId());
    }

    @GetMapping("/delete/{id}")
    public ApiResponse deleteBlog(@PathVariable Long id, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.deletePost(id, userPrincipal);
    }

    @GetMapping("/listAll")
    public ApiResponse listAllBlogs(@RequestParam Integer limit, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return blogService.listAllBlogs(limit);
    }

    @PostMapping("/comment")
    public ApiResponse comment(@Valid @RequestBody CommentRequest commentRequest, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.commentBlog(commentRequest, userPrincipal.getUserId());
    }

    @GetMapping("/like/{id}")
    public ApiResponse comment(@PathVariable Long id, @RequestParam Rating rating, Authentication authentication) {

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return blogService.likeBlog(id, rating, userPrincipal.getUserId());
    }
}
