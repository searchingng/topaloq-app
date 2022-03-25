package com.company.topaloq.service.impl;

import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.entity.PhotoEntity;
import com.company.topaloq.entity.enums.PhotoType;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.PhotoRepository;
import com.company.topaloq.service.AttachService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachServiceImpl implements AttachService {

    private final PhotoRepository photoRepository;

    @Value("${server.host}")
    private String host;

    @Value("${attach.upload}")
    private String uploadFolder;

    public AttachServiceImpl(PhotoRepository photoRepository){
        this.photoRepository = photoRepository;
    }

    @Override
    public PhotoDTO saveFile(MultipartFile multipart, PhotoType type){
        LocalDateTime dateTime = LocalDateTime.now();

        File folder = new File(uploadFolder + "/" + getFolder() + "/");

        if (!folder.exists()){
            folder.mkdirs();
        }

        String extension = getExtension(multipart.getOriginalFilename());
        String token = UUID.randomUUID().toString();

        PhotoEntity entity = new PhotoEntity();
        entity.setName(multipart.getOriginalFilename());
        entity.setContentType(multipart.getContentType());
        entity.setSize(multipart.getSize());
        entity.setExtension(extension);
        entity.setType(type);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setPath(folder.getPath() + "\\" +  token + "." + extension);
        entity.setToken(token);
        entity.setUrl("http://" + host + ":8080/attach/load/" + token);

        Path path = Paths.get(entity.getPath());

        try {
            multipart.transferTo(path);
            photoRepository.save(entity);
        } catch (IOException e) {
            throw new RuntimeException("File wasn't save!");
        }

        return toDto(entity);
    }

    PhotoEntity get(String token){
        return photoRepository.findByToken(token)
                .orElseThrow(() -> new ItemNotFoundException("Pic Not Found!!"));
    }

    PhotoEntity get(Long id){
        if (id == null)
            return null;
        return photoRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("Pic Not Found!!"));
    }

    @SneakyThrows
    @Override
    public byte[] loadAttachByToken(String token) {
        PhotoEntity entity = get(token);

        File file = new File(entity.getPath());
        if (file.exists()){
            InputStream inputStream = new FileInputStream(file);
            return inputStream.readAllBytes();
        }

        return new byte[0];
    }

    @Override
    public List<PhotoDTO> loadAttachByItemId(Long itemId) {
        return null;//photoRepository.findByItem_Id(itemId).stream()
//                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PhotoDTO getMainByItemId(Long itemId) {
        return null;//photoRepository.findByItem_IdAndIndex(itemId, 0L)
//                .map(this::toDto).orElseThrow(() -> new RuntimeException("LAL"));
    }

    public PhotoDTO getById(Long id) {
        return toDto(get(id));
    }

    @Override
    public Resource download(String name) {
        int point = name.indexOf(".");
        String token = name.substring(0, point);
        PhotoEntity entity = get(token);

        Path path = Paths.get(entity.getPath());
        try {
            Resource resource = new UrlResource(path.toUri());
            return resource;
        } catch (MalformedURLException e) {
            return download("default");
        }
    }

    @Override
    public void delete(String token) {

        PhotoEntity photo = get(token);
        File file = new File(photo.getPath());

        if (file.exists()){
            file.delete();
            photoRepository.delete(photo);
        }
    }

    private String getExtension(String name){
        int point = name.lastIndexOf(".");
        return name.substring(point + 1);
    }

    private String getFolder(){
        String folder = "";
        LocalDate date = LocalDate.now();
        return date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public PhotoDTO toDto(PhotoEntity entity){
        if (entity == null)
            return null;

        PhotoDTO dto = new PhotoDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setName(entity.getName());
        dto.setPath(entity.getPath());
        dto.setSize(entity.getSize());
        dto.setType(entity.getType());
        dto.setUrl(entity.getUrl());
        dto.setToken(entity.getToken());
        dto.setContentType(entity.getContentType());
        dto.setExtension(entity.getExtension());
        return dto;
    }

}
