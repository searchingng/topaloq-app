package com.company.topaloq.service;

import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.entity.enums.PhotoType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachService {

    PhotoDTO saveFile(MultipartFile multipart, PhotoType type);

    byte[] loadAttachByToken(String token);

    List<PhotoDTO> loadAttachByItemId(Long itemId);

    PhotoDTO getMainByItemId(Long itemId);

    Resource download(String name);

    void delete(String key);

}
