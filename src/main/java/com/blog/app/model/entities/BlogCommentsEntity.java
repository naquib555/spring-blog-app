package com.blog.app.model.entities;

import com.blog.app.model.payload.blog.CommentRequest;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BLOG_COMMENTS")
public class BlogCommentsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long commentId;

    @ManyToOne
    @JoinColumn(name = "blogId", nullable = false)
    private BlogEntity blog;

    @Basic
    @Column(name = "comment", nullable = false, length = 500)
    private String comment;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentBy")
    private UserEntity commentBy;

    public BlogCommentsEntity() {
    }

    public BlogCommentsEntity(CommentRequest commentRequest, BlogEntity blogEntity, UserEntity commentBy) {
        this.blog = blogEntity;
        this.comment = commentRequest.getComment();
        this.commentBy = commentBy;
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BlogEntity getBlog() {
        return blog;
    }

    public void setBlog(BlogEntity blog) {
        this.blog = blog;
    }

    public UserEntity getCommentBy() {
        return commentBy;
    }

    public void setCommentBy(UserEntity commentBy) {
        this.commentBy = commentBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogCommentsEntity that = (BlogCommentsEntity) o;
        return Objects.equals(commentId, that.commentId) &&
                Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, comment);
    }
}
