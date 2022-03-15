package com.company.topaloq.service;

import com.company.topaloq.dto.MessageDTO;
import com.company.topaloq.dto.filterDTO.MessageFilterDTO;
import com.company.topaloq.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageService {

    MessageDTO createMessage(Long userId, MessageDTO dto);

    void checkPermission(Long messageId, Long currentUserId);

    void deleteById(Long messageId, Long currentUserId);

    void updateById(Long messageId, Long currentUserId, MessageDTO dto);

    MessageEntity get(Long id);

    MessageDTO getById(Long id);

    Page<MessageDTO> getAll(Pageable pageable);

    Page<MessageDTO> filter(Pageable pageable, MessageFilterDTO dto);

}
