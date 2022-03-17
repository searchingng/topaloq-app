package com.company.topaloq.service.impl;

import com.company.topaloq.dto.PhotoDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachService {

    PhotoDTO saveFile(MultipartFile multipart, Long userId);

    byte[] loadAttachByToken(String token);

    List<PhotoDTO> loadAttachByItemId(Long itemId);

    PhotoDTO getMainByItemId(Long itemId);

    Resource download(String name);

}
