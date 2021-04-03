package com.blog.app.model.entities;

import com.blog.app.model.payload.blog.PostBlogRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "BLOG")
public class BlogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long blogId;

    @Basic
    @Column(name = "title", nullable = false, length = 50)
    private String title;

    @Basic
    @Column(name = "description", nullable = false)
    private String description;

    @Basic
    @Column(name = "is_ready", nullable = false)
    private Boolean isReadyForApproval;

    @Basic
    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved;

    @Basic
    @Column(name = "approved_date")
    private LocalDateTime approved_date;

    @Basic
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Basic
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Basic
    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private Set<BlogRatingEntity> likes = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private Set<BlogCommentsEntity> comments = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private UserEntity approvedBy;

    public BlogEntity() {
    }

    //create initially
    public BlogEntity(PostBlogRequest postBlogRequest, UserEntity user) {
        this.title = postBlogRequest.getTitle();
        this.description = postBlogRequest.getDescription();
        this.isReadyForApproval = false;
        this.isApproved = false;
        this.isActive = true;
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
        this.user = user;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getReadyForApproval() {
        return isReadyForApproval;
    }

    public void setReadyForApproval(Boolean readyForApproval) {
        isReadyForApproval = readyForApproval;
    }

    public Boolean getApproved() {
        return isApproved;
    }

    public void setApproved(Boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public LocalDateTime getApproved_date() {
        return approved_date;
    }

    public void setApproved_date(LocalDateTime approved_date) {
        this.approved_date = approved_date;
    }

    public Set<BlogRatingEntity> getLikes() {
        return likes;
    }

    public void setLikes(Set<BlogRatingEntity> likes) {
        this.likes = likes;
    }

    public Set<BlogCommentsEntity> getComments() {
        return comments;
    }

    public void setComments(Set<BlogCommentsEntity> comments) {
        this.comments = comments;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public UserEntity getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(UserEntity approvedBy) {
        this.approvedBy = approvedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogEntity that = (BlogEntity) o;
        return Objects.equals(blogId, that.blogId) &&
                Objects.equals(title, that.title) &&
                Objects.equals(description, that.description) &&
                Objects.equals(isActive, that.isActive) &&
                Objects.equals(isReadyForApproval, that.isReadyForApproval) &&
                Objects.equals(isApproved, that.isApproved) &&
                Objects.equals(createdDate, that.createdDate) &&
                Objects.equals(modifiedDate, that.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blogId, title, description, isActive, isReadyForApproval, isApproved, createdDate, modifiedDate);
    }
}
