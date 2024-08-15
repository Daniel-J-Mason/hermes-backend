package com.allthing.hermesbackend.application.ports.outgoing.friend;

import com.allthing.hermesbackend.application.domain.FriendRequest;

public interface CreateFriendRequestPort {
    FriendRequest createRequest(FriendRequest friendRequest);
}
