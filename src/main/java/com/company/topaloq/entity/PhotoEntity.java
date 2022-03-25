package com.company.topaloq.entity;

import com.company.topaloq.entity.enums.PhotoType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "photo")
@Setter
@Getter
@NoArgsConstructor
public class PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String path;

    private String extension;

    private Long size;

    private String contentType;

    @Enumerated(EnumType.STRING)
    private PhotoType type;

    private String url;

    private String token;

    private LocalDateTime createdDate;
}
