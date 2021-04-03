package com.blog.app.repository;

import com.blog.app.enums.Rating;
import com.blog.app.model.entities.BlogEntity;
import com.blog.app.model.entities.BlogRatingEntity;
import com.blog.app.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlogRatingRepository extends JpaRepository<BlogRatingEntity, Long> {

    Optional<BlogRatingEntity> findByBlogAndRatedBy(BlogEntity blogEntity, UserEntity userEntity);
}
