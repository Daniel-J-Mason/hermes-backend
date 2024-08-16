package com.allthing.hermesbackend.application.ports.outgoing.friend;

import com.allthing.hermesbackend.application.domain.FriendRequest;

public interface LookupFriendRequestPort {
    FriendRequest lookupFriendRequest(String senderUsername, String receiverUsername);
}
