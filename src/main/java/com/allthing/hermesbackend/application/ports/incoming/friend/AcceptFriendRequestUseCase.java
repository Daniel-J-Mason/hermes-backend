package com.allthing.hermesbackend.application.ports.incoming.friend;

public interface AcceptFriendRequestUseCase {
    boolean acceptRequest(String senderUsername, String receiverUsername);
}
