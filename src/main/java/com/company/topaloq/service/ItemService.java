package com.company.topaloq.service;

import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.filterDTO.ItemFilterDTO;
import com.company.topaloq.entity.ItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemService {

    ItemDTO createItem(ItemDTO dto, Long userId);

    void deleteById(Long id, Long currnetUserId);

    void updateById(Long id, Long currnetUserId, ItemDTO dto);

    void returnItem(Long id, Long currnetUserId);

    ItemEntity get(Long id);

    ItemDTO getById(Long id);

    List<ItemDTO> getByUserId(Long userId);

    Page<ItemDTO> getAll(Pageable pageable);

    Page<ItemDTO> filter(Pageable pageable, ItemFilterDTO dto);

    void checkPermission(Long currentUserId, Long itemId);

}
