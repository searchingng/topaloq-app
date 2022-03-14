package com.company.topaloq.config.jwt;

import com.company.topaloq.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJwtDTO {
    private Long id;
    private UserRole role;
}
