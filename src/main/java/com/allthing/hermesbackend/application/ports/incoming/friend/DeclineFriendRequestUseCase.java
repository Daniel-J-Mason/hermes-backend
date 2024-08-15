package com.allthing.hermesbackend.application.ports.incoming.friend;

public interface DeclineFriendRequestUseCase {
    boolean declineRequest(String senderUsername, String receiverUsername);
}
