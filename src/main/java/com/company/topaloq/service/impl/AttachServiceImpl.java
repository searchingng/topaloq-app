package com.company.topaloq.service.impl;

import com.company.topaloq.dto.PhotoDTO;
import com.company.topaloq.entity.ItemEntity;
import com.company.topaloq.entity.PhotoEntity;
import com.company.topaloq.exceptions.ItemNotFoundException;
import com.company.topaloq.repository.PhotoRepository;
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
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttachServiceImpl implements AttachService{

    private final PhotoRepository photoRepository;
    private final ItemServiceImpl itemService;

    @Value("${server.host}")
    private String host;

    public AttachServiceImpl(PhotoRepository photoRepository, ItemServiceImpl itemService) {
        this.photoRepository = photoRepository;
        this.itemService = itemService;
    }

    @Override
    public PhotoDTO saveFile(MultipartFile multipart, Long itemId){
        ItemEntity item = itemService.get(itemId);
        LocalDateTime dateTime = LocalDateTime.now();

        File folder = new File("uploads/" + dateTime.getYear() + "/" +
                dateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + "/");

        if (!folder.exists()){
            folder.mkdirs();
        }

        int point = multipart.getOriginalFilename().lastIndexOf(".");
        String extension = multipart.getOriginalFilename().substring(point + 1);
        String token = UUID.randomUUID().toString();

        PhotoEntity entity = new PhotoEntity();
        entity.setName(token + "." + extension);
        entity.setContentType(multipart.getContentType());
        entity.setSize(multipart.getSize());
        entity.setCreatedDate(LocalDateTime.now());
        entity.setItem(item);
        entity.setPath(folder.getPath() + "\\" +  entity.getName());
        entity.setToken(token);
        entity.setUrl("http://" + host + ":8080/attach/load/" + token);

        int index = photoRepository.countByItem_Id(itemId);
        entity.setIndex(index);

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
        return photoRepository.findByItem_Id(itemId).stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PhotoDTO getMainByItemId(Long itemId) {
        return photoRepository.findByItem_IdAndIndex(itemId, 12_541_452L)
                .map(this::toDto)
                .orElseThrow(() -> new RuntimeException("LAL"));
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

    public PhotoDTO toDto(PhotoEntity entity){
        PhotoDTO dto = new PhotoDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setName(entity.getName());
        dto.setPath(entity.getPath());
        dto.setSize(entity.getSize());
        dto.setIndex(entity.getIndex());
        dto.setItemId(entity.getItem().getId());
        dto.setUrl(entity.getUrl());
        dto.setToken(entity.getToken());
        dto.setContentType(entity.getContentType());
        return dto;
    }

}
