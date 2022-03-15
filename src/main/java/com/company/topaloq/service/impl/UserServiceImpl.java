package com.company.topaloq.service.impl;

import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.exceptions.BadRequestException;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.service.UserService;
import com.company.topaloq.spec.SpecificationBuilder;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.dto.filterDTO.UserFilterDTO;
import com.company.topaloq.repository.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO create(UserDTO dto) {

        if (userRepository.findByPhone(dto.getPhone()).isPresent())
            throw new BadRequestException("Phone is already exists");

        if (userRepository.findByLogin(dto.getLogin()).isPresent())
            throw new BadRequestException("Login is already exists");

        String password = DigestUtils.md5Hex(dto.getPassword());

        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhone(dto.getPhone());
        user.setLogin(dto.getLogin());
        user.setPassword(password);
        user.setRole(dto.getRole());

        userRepository.save(user);

        return new UserDTO(user);

    }

    @Override
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)){
            throw new ItemNotFoundException("User Not Found with thid ID");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateById(Long id, UserDTO dto) {

        if (userRepository.findByPhone(dto.getPhone()).isPresent())
            throw new BadRequestException("Phone is already exists");

        if (userRepository.findByLogin(dto.getLogin()).isPresent())
            throw new BadRequestException("Login is already exists");

        UserEntity entity = get(id);

        if (Objects.nonNull(dto.getName())){
            entity.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getSurname())){
            entity.setSurname(dto.getSurname());
        }
        if (Objects.nonNull(dto.getLogin())){
            entity.setLogin(dto.getLogin());
        }
        if (Objects.nonNull(dto.getPhone())){
            entity.setPhone(dto.getPhone());
        }
        if (Objects.nonNull(dto.getRole())){
            entity.setRole(dto.getRole());
        }
        if (Objects.nonNull(dto.getPassword())){
            entity.setPassword(dto.getPassword());
        }

        userRepository.save(entity);
    }

    @Override
    public UserEntity get(Long id){
        if (id == null || !userRepository.existsById(id)){
            throw new ItemNotFoundException("User Not Found");
        }
        return userRepository.findById(id).get();
    }

    @Override
    public UserDTO getById(Long id) {
        return toDto(get(id));
    }

    @Override
    public Page<UserDTO> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(this::toDto);
    }

    @Override
    public Page<UserDTO> filter(Pageable pageable, UserFilterDTO dto) {
        Specification<UserEntity> spec =
                new SpecificationBuilder<UserEntity>("id")
                        .equal("id", dto.getId())
                        .contains("name", dto.getName())
                        .contains("surname", dto.getSurname())
                        .startWith("login", dto.getLogin())
                        .equal("phone", dto.getPhone())
                        .equal("role", dto.getRole())
                        .fromDate("createdDate", dto.getFromDate())
                        .toDate("createdDate", dto.getToDate())
                        .build();

        return userRepository.findAll(spec, pageable).map(this::toDto);

    }

    public UserDTO toDto(UserEntity entity){
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setLogin(entity.getLogin());
        dto.setPassword(entity.getPassword());
        dto.setPhone(entity.getPhone());
        dto.setRole(entity.getRole());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }
}
