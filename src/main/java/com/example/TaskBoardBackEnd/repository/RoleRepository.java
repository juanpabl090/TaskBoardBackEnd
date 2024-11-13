package com.example.TaskBoardBackEnd.repository;

import com.example.TaskBoardBackEnd.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleNameById(@Param("id") Long id);
}