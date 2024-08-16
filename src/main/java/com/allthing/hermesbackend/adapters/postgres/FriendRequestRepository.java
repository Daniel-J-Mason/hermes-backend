package com.allthing.hermesbackend.adapters.postgres;

import com.allthing.hermesbackend.application.domain.FriendRequest;
import com.allthing.hermesbackend.application.ports.outgoing.friend.AreFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.CreateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.DeleteFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.LookupFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.UpdateFriendRequestPort;
import org.springframework.stereotype.Repository;

@Repository
public class FriendRequestRepository implements AreFriendsPort, CreateFriendRequestPort, DeleteFriendRequestPort, LookupFriendRequestPort, UpdateFriendRequestPort {
    @Override
    public boolean areFriends(String usernameFirst, String usernameSecond) {
        return false;
    }
    
    @Override
    public FriendRequest createRequest(FriendRequest friendRequest) {
        return null;
    }
    
    @Override
    public boolean deleteFriendRequest(String firstUsername, String secondUsername) {
        return false;
    }
    
    @Override
    public FriendRequest lookupFriendRequest(String senderUsername, String receiverUsername) {
        return null;
    }
    
    @Override
    public boolean updateFriendRequest(FriendRequest friendRequest) {
        return false;
    }
}
