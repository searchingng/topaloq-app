package com.company.topaloq.controller;

import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.entity.enums.PhotoType;
import com.company.topaloq.service.AttachService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController()
@RequestMapping("/attach")
public class AttachController {

    private final AttachService attachService;

    public AttachController(AttachService attachService) {
        this.attachService = attachService;
    }

    @PostMapping("/upload/{type}")
    public ResponseEntity<PhotoDTO> upload(HttpServletRequest request,
                                         @RequestParam("file") MultipartFile file,
                                         @PathVariable("type") PhotoType type){

//        UserJwtDTO jwtDTO = JwtUtil.getCurrentUser(request);
        PhotoDTO fileName = attachService.saveFile(file, type);
        return ResponseEntity.ok(fileName);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<List<PhotoDTO>> getByItemId(@PathVariable Long itemId){
        return ResponseEntity.ok(attachService.loadAttachByItemId(itemId));
    }

    @GetMapping("/main/{itemId}")
    public ResponseEntity<PhotoDTO> getMainByItemId(@PathVariable Long itemId){
        return ResponseEntity.ok(attachService.getMainByItemId(itemId));
    }

    @GetMapping(value = "/load/{token}", produces = "image/jpeg")
    public byte[] display(@PathVariable String token){
        return attachService.loadAttachByToken(token);
    }

    @GetMapping(value = "/download/{fileName:.+}")
    public ResponseEntity<Resource> download(@PathVariable("fileName") String name){
        Resource resource = attachService.download(name);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileName=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/{token}")
    public ResponseEntity<String> deleteByToken(HttpServletRequest request,
                                @PathVariable String token){

        attachService.delete(token);
        return ResponseEntity.ok("Successfully Deleted");

    }

}
