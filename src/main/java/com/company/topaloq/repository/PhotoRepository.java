package com.company.topaloq.repository;

import com.company.topaloq.entity.PhotoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends JpaRepository<PhotoEntity, Long>,
        JpaSpecificationExecutor<PhotoEntity> {



    Optional<PhotoEntity> findByToken(String token);




}
