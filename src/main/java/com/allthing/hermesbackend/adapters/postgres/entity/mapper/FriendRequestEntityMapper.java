package com.allthing.hermesbackend.adapters.postgres.entity.mapper;

import com.allthing.hermesbackend.adapters.postgres.entity.FriendRequestEntity;
import com.allthing.hermesbackend.application.domain.FriendRequest;
import com.allthing.hermesbackend.application.domain.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FriendRequestEntityMapper {
    
    private final UserEntityMapper userEntityMapper;
    
    public FriendRequestEntity map(FriendRequest friendRequest) {
        return FriendRequestEntity.builder()
                .sender(userEntityMapper.map(friendRequest.getSender()))
                .receiver(userEntityMapper.map(friendRequest.getReceiver()))
                .status(friendRequest.getStatus().toString())
                .build();
    }
    
    public FriendRequest map(FriendRequestEntity friendRequestEntity) {
        return new FriendRequest(
                userEntityMapper.map(friendRequestEntity.getSender()),
                userEntityMapper.map(friendRequestEntity.getReceiver()),
                Status.valueOf(friendRequestEntity.getStatus())
        );
    }
}
