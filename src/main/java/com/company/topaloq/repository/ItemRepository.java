package com.company.topaloq.repository;

import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.entity.enums.ItemType;
import com.company.topaloq.entity.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends
        JpaRepository<ItemEntity, Long>,
        JpaSpecificationExecutor<ItemEntity> {

    List<ItemEntity> findByUser_Id(Long userId);

    List<ItemEntity> findByStatusAndType(ItemStatus status, ItemType type);

    long countByStatus(ItemStatus status);

    List<ItemEntity> findTop10ByStatusOrderByCreatedDate(ItemStatus status);

}
