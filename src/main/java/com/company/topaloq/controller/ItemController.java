package com.company.topaloq.controller;

import com.company.topaloq.config.jwt.JwtUtil;
import com.company.topaloq.config.jwt.UserJwtDTO;
import com.company.topaloq.dto.ItemDTO;
import com.company.topaloq.dto.filterDTO.ItemFilterDTO;
import com.company.topaloq.entity.enums.ItemStatus;
import com.company.topaloq.service.ItemService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.models.auth.AuthorizationValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.service.AuthorizationScope;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.List;

import static com.company.topaloq.entity.enums.UserRole.ADMIN_ROLE;
import static com.company.topaloq.entity.enums.UserRole.USER_ROLE;

@RestController
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ApiOperation(value = "create Item", authorizations = {
            @Authorization(value = "bearer {token}")
    })
    public ResponseEntity<ItemDTO> createItem(HttpServletRequest request,
                                     @Valid @RequestBody ItemDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(itemService.createItem(dto, jwtDTO.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(HttpServletRequest request,
                                     @PathVariable Long id){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        itemService.deleteById(id, jwtDTO.getId());
        return ResponseEntity.ok("Succesfully Deleted!!!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(HttpServletRequest request,
                                     @PathVariable Long id,
                                     @RequestBody ItemDTO dto){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        itemService.updateById(id, jwtDTO.getId(), dto);
        return ResponseEntity.ok("Succesfully Saved!!!");
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<String> returnItem(HttpServletRequest request,
                                     @PathVariable Long id){
        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request, ADMIN_ROLE, USER_ROLE);
        itemService.returnItem(id, jwtDTO.getId());
        return ResponseEntity.ok("Succesfully Returned!!!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getById(HttpServletRequest request,
                                  @PathVariable Long id){
//        UserJwtDTO dto = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(itemService.getById(id));
    }

    @GetMapping("/get/{jwt}")
    public ResponseEntity<ItemDTO> getByJwtId(
                                  @PathVariable String jwt){
//        UserJwtDTO dto = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(itemService.getByJwtId(jwt));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemDTO>> getByUserId(HttpServletRequest request,
                                                     @PathVariable Long userId){
//        UserJwtDTO dto = JwtUtil.getCurrentUser(request);
        return ResponseEntity.ok(itemService.getByUserId(userId));
    }

    @GetMapping()
    public ResponseEntity<Page<ItemDTO>> getAll(Pageable pageable,
                                 HttpServletRequest request){
        return ResponseEntity.ok(itemService.getAll(pageable));
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<ItemDTO>> filter(Pageable pageable,
                                                HttpServletRequest request,
                                                @RequestBody ItemFilterDTO dto){
        return ResponseEntity.ok(itemService.filter(pageable, dto));
    }

    @GetMapping("/count/{status}")
    public ResponseEntity<Long> countByStatus(HttpServletRequest request,
                                              @PathVariable ItemStatus status){
        return ResponseEntity.ok(itemService.countByStatus(status));
    }

    @GetMapping("/last/{status}")
    public ResponseEntity<List<ItemDTO>> getLast10ByStatus(HttpServletRequest request,
                                                  @PathVariable ItemStatus status){
        return ResponseEntity.ok(itemService.getLastByStatus(status));
    }

}
