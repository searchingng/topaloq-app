package com.company.topaloq.dto;

import com.company.topaloq.entity.enums.PhotoType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PhotoDTO {

    private Long id;

    private String name;

    private String path;

    private Long size;

    private String contentType;

    private PhotoType type;

    private String extension;

    private LocalDateTime createdDate;

    private String token;
    private String url;

}
