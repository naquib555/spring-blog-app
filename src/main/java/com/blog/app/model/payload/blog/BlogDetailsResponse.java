package com.blog.app.model.payload.blog;

import com.blog.app.model.entities.BlogEntity;
import com.blog.app.model.mapper.BlogMapper;

public class BlogDetailsResponse extends BlogMapper {

    private String blogStatus;
    private String author;
    private Long authorId;
    private String approvedBy;
    private Long approvedById;

    public BlogDetailsResponse(BlogEntity blogEntity) {
        super(blogEntity);
        this.blogStatus = blogEntity.getApproved() ? "Approved" : blogEntity.getReadyForApproval() ? "Pending for approval" : "Not sent for approval";
        this.author = blogEntity.getUser().getUsername();
        this.authorId = blogEntity.getUser().getUserId();
        if(blogEntity.getApproved()) {
            this.approvedBy = blogEntity.getApprovedBy().getUsername();
            this.approvedById = blogEntity.getApprovedBy().getUserId();
        }
    }

    public String getBlogStatus() {
        return blogStatus;
    }

    public void setBlogStatus(String blogStatus) {
        this.blogStatus = blogStatus;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Long getApprovedById() {
        return approvedById;
    }

    public void setApprovedById(Long approvedById) {
        this.approvedById = approvedById;
    }
}
