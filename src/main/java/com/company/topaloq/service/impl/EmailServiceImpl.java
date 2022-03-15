package com.company.topaloq.service.impl;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.dto.EmailDTO;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.entity.EmailEntity;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.EmailStatus;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.entity.enums.UserStatus;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.EmailRepository;
import com.company.topaloq.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender javaMailSender;
    private final UserServiceImpl userService;

    @Value("${server.host}")
    private String host;

    public EmailServiceImpl(EmailRepository emailRepository,
                            JavaMailSender javaMailSender,
                            UserServiceImpl userService) {
        this.emailRepository = emailRepository;
        this.javaMailSender = javaMailSender;
        this.userService = userService;
    }

    public void send(String subject, String text, String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(text);
        message.setTo(email);
        javaMailSender.send(message);
    }

    @Override
    public EmailDTO save(UserEntity user) {

        EmailEntity entity = new EmailEntity();
        emailRepository.save(entity);

        entity.setUser(user);
        entity.setEmail(user.getEmail());
        entity.setSubject("✅Topaloq Verification");

        String jwt = JwtUtil.generateJwt(entity.getId());
        StringBuilder builder = new StringBuilder("Hello 👋 " + user.getName() +
                " " + user.getSurname());

        builder.append("\nFirstly, Thanks for register our site. You should ");
        builder.append("verify your account to use fully service.\n");
        builder.append("Click link below to verify your account 👇👇👇\n");
        builder.append("http://" + host + ":8080/auth/verify/" + jwt);

        entity.setText(builder.toString());

        emailRepository.save(entity);

        return toDto(entity);
    }

    @Override
    public EmailDTO send(UserEntity user) {
        EmailDTO dto = save(user);
        send(dto.getSubject(), dto.getText(), dto.getEmail());
        return dto;
    }

    @Override
    public EmailEntity get(Long id) {
        if (id == null || !emailRepository.existsById(id)){
            throw new ItemNotFoundException("Such verification message hasn't found");
        }
        return emailRepository.findById(id).get();
    }

    @Override
    public EmailEntity verify(Long id) {
         EmailEntity entity = get(id);
         entity.setStatus(EmailStatus.VERIFIED);
         entity.setVerifiedDate(LocalDateTime.now());
         emailRepository.save(entity);
         return entity;
    }

    @Override
    public Page<EmailDTO> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public EmailDTO getById(Long id) {
        return null;
    }

    private EmailDTO toDto(EmailEntity entity){
        EmailDTO dto = new EmailDTO();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setSubject(entity.getSubject());
        dto.setText(entity.getText());
        dto.setUserId(entity.getUser().getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setStatus(entity.getStatus());
        dto.setVerifiedDate(entity.getVerifiedDate());

        return dto;
    }
}
