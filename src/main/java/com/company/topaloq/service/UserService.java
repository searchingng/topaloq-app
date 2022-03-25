package com.company.topaloq.service;

import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.dto.filterDTO.UserFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDTO create(UserDTO dto);

    void deleteById(Long id);

    void updateById(Long id, UserDTO dto);

    UserEntity get(Long id);

    UserDTO getById(Long id);

    PhotoDTO getPhotoByUserId(Long userId);

    Page<UserDTO> getAll(Pageable pageable);

    Page<UserDTO> filter(Pageable pageable, UserFilterDTO dto);

}
