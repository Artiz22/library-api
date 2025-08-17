package com.example.libraryApi.repository;

import com.example.libraryApi.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findOneWithRolesByLoginIgnoreCase(String login);
}
