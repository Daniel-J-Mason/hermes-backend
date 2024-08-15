package com.allthing.hermesbackend.application.ports.incoming.friend;

//This also servers to delete a friend as well
public interface DeleteFriendRequestUseCase {
    boolean deleteRequest(String senderUsername, String receiverUsername);
}
