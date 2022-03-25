package com.company.topaloq.dto;

import com.company.topaloq.entity.PhotoEntity;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.entity.enums.ItemType;
import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    private String description;

    private String foundAddress;

    private Long userId;

    @NotNull(message = "status should be FOUND or LOST")
    private ItemStatus status;
    private ItemType type;
    private LocalDateTime createdDate;
    private LocalDateTime returnedDate;

    private Set<Long> photos;
}
