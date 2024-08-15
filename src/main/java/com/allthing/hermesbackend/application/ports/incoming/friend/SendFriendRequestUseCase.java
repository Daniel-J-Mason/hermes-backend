package com.allthing.hermesbackend.application.ports.incoming.friend;

import com.allthing.hermesbackend.application.domain.FriendRequest;

public interface SendFriendRequestUseCase {
    FriendRequest sendRequest(String senderUsername, String receiverUsername);
}
