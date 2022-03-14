package com.company.topaloq.dto.filterDTO;

import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.entity.enums.ItemType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ItemFilterDTO {

    private Long id;
    private String name;
    private String description;
    private String foundAddress;
    private Long userId;
    private ItemStatus status;
    private ItemType type;
    private LocalDate fromDate;
    private LocalDate toDate;

}
