package com.blog.app.model.entities;

import com.blog.app.enums.Rating;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "BLOG_LIKES")
public class BlogRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long likeId;

    @Basic
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false, length = 10)
    private Rating rating;

    @ManyToOne
    @JoinColumn(name = "blogId", nullable = false)
    private BlogEntity blog;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ratedBy")
    private UserEntity ratedBy;

    public BlogRatingEntity() {
    }

    public BlogRatingEntity(Rating rating, BlogEntity blog, UserEntity ratedBy) {
        this.rating = rating;
        this.blog = blog;
        this.ratedBy = ratedBy;
    }

    public Long getLikeId() {
        return likeId;
    }

    public void setLikeId(Long likeId) {
        this.likeId = likeId;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public BlogEntity getBlog() {
        return blog;
    }

    public void setBlog(BlogEntity blog) {
        this.blog = blog;
    }

    public UserEntity getRatedBy() {
        return ratedBy;
    }

    public void setRatedBy(UserEntity ratedBy) {
        this.ratedBy = ratedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlogRatingEntity that = (BlogRatingEntity) o;
        return Objects.equals(likeId, that.likeId) &&
                Objects.equals(rating, that.rating);
    }

    @Override
    public int hashCode() {
        return Objects.hash(likeId, rating);
    }
}
