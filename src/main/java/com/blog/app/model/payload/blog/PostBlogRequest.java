package com.blog.app.model.payload.blog;

import com.blog.app.model.mapper.BlogMapper;

import java.util.StringJoiner;

public class PostBlogRequest {

    private String title;
    private String description;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PostBlogRequest.class.getSimpleName() + "[", "]")
                .add("title='" + title + "'")
                .add("description='" + description + "'")
                .toString();
    }
}
