package com.company.topaloq.service.impl;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.dto.filterDTO.ItemFilterDTO;
import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.PhotoEntity;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.entity.enums.ItemType;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.exceptions.ForbiddenException;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.ItemRepository;
import com.company.topaloq.service.ItemService;
import com.company.topaloq.spec.SpecificationBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.company.topaloq.entity.enums.ItemStatus.FOUND;
import static com.company.topaloq.entity.enums.ItemStatus.LOST;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final UserServiceImpl userService;
    private final ItemRepository itemRepository;
    private final EmailServiceImpl emailService;
    private final AttachServiceImpl attachService;


    @Override
    public ItemDTO createItem(ItemDTO dto, Long userId) {
        UserEntity user = userService.get(userId);

        ItemEntity entity = new ItemEntity();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setFoundAddress(dto.getFoundAddress());
        entity.setStatus(dto.getStatus());
        entity.setUser(user);
        entity.setPhotos(
                dto.getPhotos().stream().map(attachService::get)
                        .collect(Collectors.toSet())
        );
        if (!Objects.isNull(dto.getType())){
            entity.setType(dto.getType());
        }

        try {
            emailService.sendIdentityItems(entity, entity.getUser().getEmail(),
                    findByStatusAndType(entity.getStatus(), entity.getType()));
        } catch (Exception e) {
            System.out.println("Email exp");
        }

        itemRepository.save(entity);
        dto.setId(entity.getId());
        dto.setType(entity.getType());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }

    @Override
    public List<PhotoDTO> getPhotosByItemId(Long itemId){
        ItemEntity item = get(itemId);
        return item.getPhotos().stream()
                .map(attachService::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemEntity get(Long id) {
        if (id == null || !itemRepository.existsById(id)) {
            throw new ItemNotFoundException("Item Not Found");
        }
        return itemRepository.findById(id).get();
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

            if (dto.getStatus().equals(ItemStatus.RETURNED))
                item.setReturnedDate(LocalDateTime.now());
        }
        if (Objects.nonNull(dto.getType())){
            item.setType(dto.getType());
        }
        itemRepository.save(item);
    }

    @Override
    public void returnItem(Long id, Long currnetUserId) {
        checkPermission(currnetUserId, id);
        ItemEntity item = get(id);
        item.setStatus(ItemStatus.RETURNED);
        item.setReturnedDate(LocalDateTime.now());
        itemRepository.save(item);
    }

    @Override
    public ItemDTO getById(Long id) {
        return toDto(get(id));
    }

    @Override
    public ItemDTO getByJwtId(String jwt) {
        Long id = JwtUtil.parseLongJwt(jwt);
        return getById(id);
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

        UserEntity user = null;
        if (Objects.nonNull(dto.getUserId())){
            user = userService.get(dto.getUserId());
        }

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
                .fromDate("returnedDate", dto.getFromReturnedDate())
                .toDate("returnedDate", dto.getToReturnedDate())
                .build();

        return itemRepository.findAll(spec, pageable).map(this::toDto);

    }

    @Override
    public void checkPermission(Long currentUserId, Long itemId){
        ItemEntity item = get(itemId);
        UserEntity currentUser = userService.get(currentUserId);

        if (!currentUser.getRole().equals(UserRole.ADMIN_ROLE) &&
                !item.getUser().getId().equals(currentUserId))
            throw new ForbiddenException("You can modify only your items");
    }

    @Override
    public List<ItemDTO> findByStatusAndType(ItemStatus status, ItemType type) {
        status = (status.equals(LOST)) ? FOUND : LOST;
        return itemRepository.findByStatusAndType(status, type)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public long countByStatus(ItemStatus status) {
        return itemRepository.countByStatus(status);
    }

    @Override
    public List<ItemDTO> getLastByStatus(ItemStatus status) {
        return itemRepository.findTop10ByStatusOrderByCreatedDate(status)
                .stream().map(this::toDto).collect(Collectors.toList());
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
        dto.setPhotos(entity.getPhotos()
                .stream().map(PhotoEntity::getId).collect(Collectors.toSet()));

        return dto;
    }
}
