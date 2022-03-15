package com.company.topaloq.dto;

import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageDTO {

    private Long id;

    @NotNull(message = "Content must not be null")
    @NotBlank(message = "Content must not be empty")
    private String content;

    private Long userId;

    @NotNull(message = "Qaysi Itemga Bu (itemId - ?")
    private Long itemId;

    private LocalDateTime createdDate = LocalDateTime.now();

}
