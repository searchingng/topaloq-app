package com.company.topaloq.dto;

import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.EmailStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmailDTO {

    private Long id;

    private String subject;

    private String text;

    private String email;

    private Long userId;

    private EmailStatus status;

    private LocalDateTime createdDate;

    private LocalDateTime verifiedDate;

}
