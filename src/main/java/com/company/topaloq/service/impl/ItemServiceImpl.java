package com.company.topaloq.service.impl;

import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.filterDTO.ItemFilterDTO;
import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.exceptions.ForbiddenException;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.ItemRepository;
import com.company.topaloq.service.ItemService;
import com.company.topaloq.spec.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;

    public ItemServiceImpl(UserServiceImpl userService, ItemRepository itemRepository) {
        this.userService = userService;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO createItem(ItemDTO dto, Long userId) {
        UserEntity user = userService.get(userId);

        ItemEntity entity = new ItemEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFoundAddress(dto.getFoundAddress());
        entity.setStatus(dto.getStatus());
        entity.setUser(user);
        if (!Objects.isNull(dto.getType())){
            entity.setType(dto.getType());
        }

        itemRepository.save(entity);
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    @Override
    public ItemEntity get(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Item Not Found"));
    }

    @Override
    public void deleteById(Long id, Long userId) {
        ItemEntity item = get(id);
        checkPermission(userId, id);
        itemRepository.delete(item);
    }

    @Override
    public void updateById(Long id, Long currnetUserId, ItemDTO dto) {
        ItemEntity item = get(id);

        checkPermission(currnetUserId, id);

        if (Objects.nonNull(dto.getName())){
            item.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getDescription())){
            item.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getFoundAddress())){
            item.setFoundAddress(dto.getFoundAddress());
        }
        if (Objects.nonNull(dto.getStatus())){
            item.setStatus(dto.getStatus());
        }
        if (Objects.nonNull(dto.getType())){
            item.setType(dto.getType());
        }
        itemRepository.save(item);
    }

    @Override
    public ItemDTO getById(Long id) {
        return toDto(get(id));
    }

    @Override
    public List<ItemDTO> getByUserId(Long userId) {
        List<ItemEntity> items = itemRepository.findByUser_Id(userId);
        return items.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public Page<ItemDTO> getAll(Pageable pageable) {
        return itemRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public Page<ItemDTO> filter(Pageable pageable, ItemFilterDTO dto) {

        UserEntity user = userService.get(dto.getUserId());

                SpecificationBuilder<ItemEntity> builder =
                new SpecificationBuilder<ItemEntity>("id");

        Specification<ItemEntity> spec = builder
                .equal("id", dto.getId())
                .contains("name", dto.getName())
                .contains("description", dto.getDescription())
                .contains("foundAddress", dto.getFoundAddress())
                .equal("status", dto.getStatus())
                .equal("type", dto.getType())
                .equal("user", user)
                .fromDate("createdDate", dto.getFromDate())
                .toDate("createdDate", dto.getToDate())
                .build();

        return itemRepository.findAll(spec, pageable).map(this::toDto);

    }

    @Override
    public void checkPermission(Long currentUserId, Long itemId){
        ItemEntity item = get(itemId);
        if (!item.getUser().getId().equals(currentUserId)){
            throw new ForbiddenException("You can delete only your items");
        }
    }

    public ItemDTO toDto(ItemEntity entity){
        ItemDTO dto = new ItemDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setFoundAddress(entity.getFoundAddress());
        dto.setStatus(entity.getStatus());
        dto.setType(entity.getType());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUserId(entity.getUser().getId());

        return dto;
    }
}
