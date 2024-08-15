package com.allthing.hermesbackend.application.ports.incoming.user;

import com.allthing.hermesbackend.application.domain.User;

import java.util.List;

public interface ListFriendsUseCase {
    List<User> listFriends(String username);
}
