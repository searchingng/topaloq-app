package com.company.topaloq.service;

import com.company.topaloq.dto.EmailDTO;
import com.company.topaloq.entity.EmailEntity;
import com.company.topaloq.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EmailService {

    void send(String subject, String text, String  email);

    EmailDTO save(UserEntity user);

    EmailDTO send(UserEntity user);

    EmailEntity verify(Long id);

    Page<EmailDTO> getAll(Pageable pageable);

    EmailEntity get(Long id);

    EmailDTO getById(Long id);

}
