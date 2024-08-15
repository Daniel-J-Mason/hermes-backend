package com.allthing.hermesbackend.application.ports.outgoing.friend;

public interface DeleteFriendRequestPort {
    boolean deleteFriendRequest(String firstUsername, String secondUsername);
}
