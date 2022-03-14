package com.company.topaloq.repository;

import com.company.topaloq.entity.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends
        JpaRepository<ItemEntity, Long>,
        JpaSpecificationExecutor<ItemEntity> {

    List<ItemEntity> findByUser_Id(Long userId);

}
