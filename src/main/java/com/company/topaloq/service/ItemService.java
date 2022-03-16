package com.company.topaloq.service;

import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.filterDTO.ItemFilterDTO;
import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.entity.enums.ItemType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO dto, Long userId);

    void deleteById(Long id, Long currnetUserId);

    void updateById(Long id, Long currnetUserId, ItemDTO dto);

    void returnItem(Long id, Long currnetUserId);

    ItemEntity get(Long id);

    ItemDTO getById(Long id);

    ItemDTO getByJwtId(String jwt);

    List<ItemDTO> getByUserId(Long userId);

    Page<ItemDTO> getAll(Pageable pageable);

    Page<ItemDTO> filter(Pageable pageable, ItemFilterDTO dto);

    void checkPermission(Long currentUserId, Long itemId);

    List<ItemDTO> findByStatusAndType(ItemStatus status, ItemType type);

    long countByStatus(ItemStatus status);

    List<ItemDTO> getLastByStatus(ItemStatus status);

}
