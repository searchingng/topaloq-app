package com.company.topaloq.entity;

import com.company.topaloq.entity.enums.EmailStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "email")
@Setter
@Getter
@NoArgsConstructor
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String text;

    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    private EmailStatus status = EmailStatus.SENT;

    private LocalDateTime createdDate = LocalDateTime.now();

    private LocalDateTime verifiedDate;
}
