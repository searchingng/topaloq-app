package com.company.topaloq.entity;

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

    private Long size;

    private String contentType;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemEntity item;

    private int index;

    private String url;

    private String token;

    private LocalDateTime createdDate;
}
