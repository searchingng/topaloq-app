package com.company.topaloq.dto.filterDTO;

import com.company.topaloq.entity.enums.UserRole;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserFilterDTO {

    private Long id;
    private String name;
    private String surname;
    private String login;
    private String phone;
    private UserRole role;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String orderBy;

}
