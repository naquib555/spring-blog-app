package com.blog.app.repository;

import com.blog.app.model.entities.RoleEntity;
import com.blog.app.model.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUserId(Long userId);
    Optional<UserEntity> findByUserIdAndAndIsActive(Long userId, Integer isActive);
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByUsernameAndIsActive(String username, Integer isActive);

    Boolean existsByUsername(String username);

//    List<UserEntity> findAllByIsActive(Integer isActive);
//    List<UserEntity> findAllByIsActiveAndRoles(Integer isActive, Set<RoleEntity> roles);



}
