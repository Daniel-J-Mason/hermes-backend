package com.allthing.hermesbackend.adapters.postgres.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendRequestEntity {
    private UserEntity sender;
    private UserEntity receiver;
    private String status;
}
