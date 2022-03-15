package com.company.topaloq.service.impl;

import com.company.topaloq.dto.MessageDTO;
import com.company.topaloq.dto.filterDTO.MessageFilterDTO;
import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.MessageEntity;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.exceptions.ForbiddenException;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.MessageRepository;
import com.company.topaloq.service.MessageService;
import com.company.topaloq.spec.SpecificationBuilder;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;

    public MessageServiceImpl(MessageRepository messageRepository,
                              UserServiceImpl userService,
                              ItemServiceImpl itemService) {
        this.messageRepository = messageRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public MessageDTO createMessage(Long userId, MessageDTO dto) {
        UserEntity user = userService.get(userId);
        ItemEntity item = itemService.get(dto.getItemId());

        MessageEntity entity = new MessageEntity();
        entity.setContent(dto.getContent());
        entity.setItem(item);
        entity.setUser(user);
        messageRepository.save(entity);

        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUserId(userId);
        return dto;
    }

    @Override
    public void checkPermission(Long messageId, Long currentUserId) {
        UserEntity currentUser = userService.get(currentUserId);
        MessageEntity message = get(messageId);

        if (!message.getUser().getId().equals(currentUserId) &&
                !currentUser.getRole().equals(UserRole.ADMIN_ROLE)) {
            throw new ForbiddenException("This permission is denied for you");
        }
    }

    @Override
    public void deleteById(Long messageId, Long currentUserId) {
        checkPermission(messageId, currentUserId);
        messageRepository.deleteById(messageId);
    }

    @Override
    public MessageEntity get(Long id) {
        if (id == null || !messageRepository.existsById(id)){
            throw new ItemNotFoundException("Message Not Found");
        }
        return messageRepository.findById(id).get();
    }

    @Override
    public void updateById(Long messageId, Long currentUserId, MessageDTO dto) {
        checkPermission(messageId, currentUserId);
        MessageEntity message = get(messageId);

        if (dto.getItemId() != null) {
            ItemEntity item = itemService.get(dto.getItemId());
            message.setItem(item);
        }
        if (dto.getContent() != null){
            message.setContent(dto.getContent());
        }

        messageRepository.save(message);
    }

    @Override
    public MessageDTO getById(Long id) {
        return toDto(get(id));
    }

    @Override
    public Page<MessageDTO> getAll(Pageable pageable) {
        return messageRepository.findAll(pageable)
                .map(this::toDto);
    }

    @Override
    public Page<MessageDTO> filter(Pageable pageable, MessageFilterDTO dto) {
        UserEntity user = null;
        ItemEntity item = null;

        if (dto.getUserId() != null)
            user = userService.get(dto.getUserId());
        if (dto.getItemId() != null)
            item = itemService.get(dto.getItemId());

        SpecificationBuilder<MessageEntity> builder =
                new SpecificationBuilder<MessageEntity>("id");

        Specification<MessageEntity> spec = builder
                .equal("id", dto.getId())
                .contains("content", dto.getContent())
                .equal("user", user)
                .equal("item", item)
                .fromDate("createdDate", dto.getFromDate())
                .toDate("createdDate", dto.getToDate())
                .build();

        return messageRepository.findAll(spec, pageable).map(this::toDto);
    }

    /*public List<MessageDTO> getByUserId(Long userId) {
        return messageRepository.findByUser_Id(userId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<MessageDTO> getByItemId(Long itemId) {
        return messageRepository.findByItem_Id(itemId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }*/

    private MessageDTO toDto(MessageEntity entity){
        MessageDTO dto = new MessageDTO();
        dto.setId(entity.getId());
        dto.setContent(entity.getContent());
        dto.setUserId(entity.getUser().getId());
        dto.setItemId(entity.getItem().getId());
        dto.setCreatedDate(entity.getCreatedDate());

        return dto;
    }
}
