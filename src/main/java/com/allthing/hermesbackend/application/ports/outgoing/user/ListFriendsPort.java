package com.allthing.hermesbackend.application.ports.outgoing.user;

import com.allthing.hermesbackend.application.domain.User;

import java.util.List;

public interface ListFriendsPort {
    List<User> listFriends(String username);
    
}
