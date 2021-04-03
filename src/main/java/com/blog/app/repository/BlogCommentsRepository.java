package com.blog.app.repository;

import com.blog.app.model.entities.BlogCommentsEntity;
import com.blog.app.model.entities.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogCommentsRepository extends JpaRepository<BlogCommentsEntity, Long> {
}
