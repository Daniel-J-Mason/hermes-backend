package com.allthing.hermesbackend.application.ports.outgoing.friend;

import com.allthing.hermesbackend.application.domain.FriendRequest;

public interface UpdateFriendRequestPort {
    boolean updateFriendRequest(FriendRequest friendRequest);
}
