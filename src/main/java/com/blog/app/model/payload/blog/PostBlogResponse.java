package com.blog.app.model.payload.blog;

import java.util.StringJoiner;

public class PostBlogResponse {

    private Long blogId;

    public PostBlogResponse(Long blogId) {
        this.blogId = blogId;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PostBlogResponse.class.getSimpleName() + "[", "]")
                .add("blogId=" + blogId)
                .toString();
    }
}
