package com.company.topaloq.repository;

import com.company.topaloq.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmailRepository extends JpaRepository<EmailEntity, Long>,
        JpaSpecificationExecutor<EmailEntity> {



}
