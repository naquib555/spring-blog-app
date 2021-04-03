package com.blog.app.repository;


import com.blog.app.enums.RoleName;
import com.blog.app.model.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleEntity, RoleName> {

    Optional<RoleEntity> findByRoleName(RoleName roleName);
}
