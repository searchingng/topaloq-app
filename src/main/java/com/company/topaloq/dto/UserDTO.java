package com.company.topaloq.dto;

import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.entity.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    private Long id;

    private String name;
    private String surname;
    private String login;
    private String password;
    private String phone;

    private UserRole role;
    private UserStatus status;

    private LocalDateTime createdDate = LocalDateTime.now();
    private String jwt;
    private Long photoId;

    public UserDTO(UserEntity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.surname = entity.getSurname();
        this.login = entity.getLogin();
        this.password = entity.getPassword();
        this.phone = entity.getPhone();
        this.role = entity.getRole();
        this.status = entity.getStatus();
        this.createdDate = entity.getCreatedDate();
        this.photoId = entity.getPhoto().getId();
    }
}
