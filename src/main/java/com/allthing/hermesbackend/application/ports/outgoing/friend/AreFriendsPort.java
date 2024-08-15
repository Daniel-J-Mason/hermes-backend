package com.allthing.hermesbackend.application.ports.outgoing.friend;

public interface AreFriendsPort {
    boolean areFriends(String usernameFirst, String usernameSecond);
}
