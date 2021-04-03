package com.blog.app.model.payload.blog;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.StringJoiner;

public class CommentRequest {

    @NotNull
    private Long blogId;

    @Size(min = 1, max = 500)
    private String comment;

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommentRequest.class.getSimpleName() + "[", "]")
                .add("blogId=" + blogId)
                .add("comment='" + comment + "'")
                .toString();
    }
}
