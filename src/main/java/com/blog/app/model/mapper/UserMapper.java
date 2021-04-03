package com.blog.app.model.mapper;


import com.blog.app.model.UserInfo;
import com.blog.app.model.entities.UserEntity;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class UserMapper extends UserInfo {

    private Long id;

    private Integer active;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private Set<String> authorities;

    public UserMapper() {
    }

    public UserMapper(UserEntity userEntity) {
        this.id = userEntity.getUserId();
        this.username = userEntity.getUsername();
        this.active = userEntity.getIsActive();
        this.createdDate = userEntity.getCreatedDate();
        this.lastModifiedDate = userEntity.getModifiedDate();
        this.authorities = userEntity.getRoles()
                .stream()
                .map(x-> x.getRoleName().toString())
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", UserMapper.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("active=" + active)
                .add("createdDate=" + createdDate)
                .add("lastModifiedDate=" + lastModifiedDate)
                .add("authorities=" + authorities)
                .toString();
    }
}
