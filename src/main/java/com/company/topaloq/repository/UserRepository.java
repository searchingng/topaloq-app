package com.company.topaloq.repository;

import com.company.topaloq.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends
        JpaRepository<UserEntity, Long>,
        JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByPhone(String phone);

    Optional<UserEntity> findByLogin(String login);

    Optional<UserEntity> findByLoginAndPassword(String Login, String password);

}
