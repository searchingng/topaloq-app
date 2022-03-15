package com.company.topaloq.repository;

import com.company.topaloq.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<MessageEntity, Long>,
        JpaSpecificationExecutor<MessageEntity> {

    List<MessageEntity> findByUser_Id(Long userId);

    List<MessageEntity> findByItem_Id(Long itemId);
}
