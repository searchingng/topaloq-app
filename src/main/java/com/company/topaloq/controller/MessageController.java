package com.company.topaloq.controller;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.config.jwt.UserJwtDTO;
import com.company.topaloq.dto.MessageDTO;
import com.company.topaloq.dto.filterDTO.MessageFilterDTO;
import com.company.topaloq.entity.enums.UserRole;
import com.company.topaloq.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.company.topaloq.entity.enums.UserRole.ADMIN_ROLE;
import static com.company.topaloq.entity.enums.UserRole.USER_ROLE;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(HttpServletRequest request,
                                                    @Valid @RequestBody MessageDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(messageService.createMessage(jwtDTO.getId(), dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteById(HttpServletRequest request,
                                             @PathVariable Long id){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        messageService.deleteById(id, jwtDTO.getId());
        return ResponseEntity.ok("Successfully Deleted!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateById(HttpServletRequest request,
                                             @PathVariable Long id,
                                             @RequestBody MessageDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        messageService.updateById(id, jwtDTO.getId(), dto);
        return ResponseEntity.ok("Successfully Saved!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDTO> getById(HttpServletRequest request,
                                              @PathVariable Long id){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(messageService.getById(id));
    }

    @GetMapping
    public ResponseEntity<Page<MessageDTO>> getAll(Pageable pageable,
                                                   HttpServletRequest request){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE);
        return ResponseEntity.ok(messageService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<MessageDTO>> filter(Pageable pageable,
                                                   HttpServletRequest request,
                                                   @RequestBody MessageFilterDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE);
        return ResponseEntity.ok(messageService.filter(pageable, dto));
    }

}
