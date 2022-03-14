package com.company.topaloq.controller;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.config.jwt.UserJwtDTO;
import com.company.topaloq.dto.UserDTO;
import com.company.topaloq.service.UserService;
import com.company.topaloq.dto.filterDTO.UserFilterDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.company.topaloq.entity.enums.UserRole.ADMIN_ROLE;
import static com.company.topaloq.entity.enums.UserRole.USER_ROLE;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(HttpServletRequest request,
                                     @RequestBody UserDTO dto){

        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE);
        return ResponseEntity.ok(userService.create(dto));
    }

    @PutMapping
    public ResponseEntity update(HttpServletRequest request,
                                 @RequestBody UserDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        userService.updateById(jwtDTO.getId(), dto);
        return ResponseEntity.ok("Successfully Saved");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(HttpServletRequest request,
                                     @PathVariable Long id){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE);
        userService.deleteById(id);
        return ResponseEntity.ok("SUCCESSFULLY");
    }

    @GetMapping
    public ResponseEntity getAll(Pageable pageable,
                                 HttpServletRequest request){

        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @PostMapping("/filter")
    public ResponseEntity filter(Pageable pageable,
                                 @RequestBody UserFilterDTO dto,
                                 HttpServletRequest request){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(userService.filter(pageable, dto));
    }

}
