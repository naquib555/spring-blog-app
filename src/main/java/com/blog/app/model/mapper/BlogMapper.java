package com.blog.app.model.mapper;

import com.blog.app.model.entities.BlogEntity;

import java.time.LocalDateTime;

public class BlogMapper {

    private Long blogId;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private LocalDateTime approvedAt;

    public BlogMapper() {
    }

    public BlogMapper(BlogEntity blogEntity) {
        this.blogId = blogEntity.getBlogId();
        this.title = blogEntity.getTitle();
        this.description = blogEntity.getDescription();
        this.createdAt = blogEntity.getCreatedDate();
        this.modifiedAt = blogEntity.getModifiedDate();
    }

    public Long getBlogId() {
        return blogId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
