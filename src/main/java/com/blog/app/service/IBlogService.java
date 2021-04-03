package com.blog.app.service;

import com.blog.app.enums.Rating;
import com.blog.app.model.payload.ApiResponse;
import com.blog.app.model.payload.blog.CommentRequest;
import com.blog.app.model.payload.blog.PostBlogRequest;
import com.blog.app.security.domain.UserPrincipal;

public interface IBlogService {

    ApiResponse postBlog(PostBlogRequest postBlogRequest, Long userId);
    ApiResponse sendForApproval(Long blogId, Long userId);
    ApiResponse approveBlog(Long blogId, Long userId);
    ApiResponse deletePost(Long blogId, UserPrincipal userPrincipal);
    ApiResponse listAllBlogs(Integer searchLimit);
    ApiResponse getBlog(Long blogId);
    ApiResponse likeBlog(Long blogId, Rating rating, Long userId);
    ApiResponse commentBlog(CommentRequest commentRequest, Long userId);
}
