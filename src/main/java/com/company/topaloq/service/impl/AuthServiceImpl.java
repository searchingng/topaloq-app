package com.company.topaloq.service.impl;

import com.company.topaloq.dto.auth.AuthDTO;
import com.company.topaloq.dto.auth.RegistrationDTO;
import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.entity.EmailEntity;
import com.company.topaloq.entity.enums.UserStatus;
import com.company.topaloq.exceptions.BadRequestException;
import com.company.topaloq.exceptions.UnauthorizedException;
import com.company.topaloq.service.AuthService;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.entity.UserEntity;
import com.company.topaloq.repository.UserRepository;
import com.company.topaloq.entity.enums.UserRole;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final EmailServiceImpl emailService;

    public AuthServiceImpl(UserRepository userRepository, EmailServiceImpl emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }


    @Override
    public UserDTO registration(RegistrationDTO dto) {

        if (userRepository.findByPhone(dto.getPhone()).isPresent())
            throw new BadRequestException("Phone is already exists");

        if (userRepository.findByLogin(dto.getLogin()).isPresent())
            throw new BadRequestException("Login is already exists");

        if (userRepository.findByEmail(dto.getEmail()).isPresent())
            throw new BadRequestException("Email is already exists");

        String password = DigestUtils.md5Hex(dto.getPassword());

        UserEntity user = new UserEntity();
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPhone(dto.getPhone());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setPassword(password);
        user.setRole(UserRole.USER_ROLE);
        user.setStatus(UserStatus.CREATED);

        userRepository.save(user);

        emailService.send(user);

        return new UserDTO(user);
    }

    @Override
    public UserDTO login(AuthDTO dto) {

        String password = DigestUtils.md5Hex(dto.getPassword());

        UserEntity user = userRepository
                .findByLoginAndPassword(dto.getLogin(), password)
                .orElseThrow(() -> new UnauthorizedException("Login Or Password is incorrect"));

        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setSurname(user.getSurname());
        userDTO.setPhone(user.getPhone());
        userDTO.setJwt(JwtUtil.generateJwt(user.getId(), user.getRole()));

        return userDTO;
    }

    @Override
    public void verify(String jwt) {
        try {
            Long id = JwtUtil.parseLongJwt(jwt);
            EmailEntity entity = emailService.verify(id);

            UserEntity user = entity.getUser();
            user.setStatus(UserStatus.ACTIVE);
            userRepository.save(user);

        } catch (RuntimeException ex){
            throw new RuntimeException("VERIFICATION FAILED");
        }

    }
}
