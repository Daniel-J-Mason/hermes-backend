package com.allthing.hermesbackend.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageEntity {
    private UUID id;
    private UUID publicId;
    private String title;
    private String body;
    private String scheduledTime;
    private UserEntity creator;
}
