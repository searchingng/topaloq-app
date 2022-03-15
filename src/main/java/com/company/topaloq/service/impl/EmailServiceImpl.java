package com.company.topaloq.service.impl;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.dto.EmailDTO;
import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.entity.EmailEntity;
import com.company.topaloq.entity.ItemEntity;
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
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;

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
    public void sendIdentityItems(ItemEntity entity, String email, List<ItemDTO> dtoList) throws MessagingException {
        String status = entity.getStatus().name().toLowerCase();
        String inverse = (status.equals("lost")) ? "found" : "lost";

        String subject = "🔎 " + entity.getName() + " may be " + inverse +
                ", that You have " + status + "!";

        StringBuilder builder = new StringBuilder("<h1>" + status + " " + entity.getName());
        builder.append(" might be items below:");
        builder.append("</h1><br>");


        dtoList.stream().forEach(i -> {
            String jwt = JwtUtil.generateJwt(i.getId());

            builder.append("<h2><a href=\"http://" + host +":8080/item/get/" + jwt + "\">");
            builder.append("<font color=navy><b>" + i.getName() + "</b></font></a></h2>");
            builder.append("<h3><font><i>" + i.getDescription() + "</i></font></h3>");
            builder.append("<h3><font color=gray>" + i.getFoundAddress() + "</font></h3>");
            builder.append("<br>");
        });

        MimeMessage message = javaMailSender.createMimeMessage();
        message.setSubject(subject);
//        message.setText(builder.toString());
        message.setRecipients(Message.RecipientType.TO, email);
        message.setContent(builder.toString(), "text/html");
        javaMailSender.send(message);

        /*SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(subject);
        message.setText(builder.toString());
        message.setTo(email);*/

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
        return emailRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public EmailDTO getById(Long id) {
        return toDto(get(id));
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
