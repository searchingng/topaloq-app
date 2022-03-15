package com.company.topaloq.dto.filterDTO;

import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageFilterDTO {

    private Long id;

    private String content;

    private Long userId;

    private Long itemId;

    private LocalDate fromDate;

    private LocalDate toDate;

}
