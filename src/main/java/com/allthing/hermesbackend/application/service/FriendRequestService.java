package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.FriendRequest;
import com.allthing.hermesbackend.application.domain.Status;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestCreationException;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestDeletionException;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestUpdateException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.friend.AcceptFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.incoming.friend.DeclineFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.incoming.friend.DeleteFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.incoming.friend.SendFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.CreateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.DeleteFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.LookupFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.UpdateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendRequestService implements AcceptFriendRequestUseCase, DeclineFriendRequestUseCase, DeleteFriendRequestUseCase, SendFriendRequestUseCase {
    
    private final CreateFriendRequestPort createFriendRequestPort;
    private final DeleteFriendRequestPort deleteFriendRequestPort;
    private final UpdateFriendRequestPort updateFriendRequestPort;
    private final LookupFriendRequestPort lookupFriendRequestPort;
    private final FindUserPort findUserPort;
    
    @Override
    public boolean acceptRequest(String senderUsername, String receiverUsername) {
        Pair<User, User> users = checkUsersExist(senderUsername, receiverUsername);
        
        FriendRequest friendRequest = lookupFriendRequestPort.lookupFriendRequest(senderUsername, receiverUsername);
        friendRequest.acceptFriendRequest();
        
        boolean isUpdated = updateFriendRequestPort.updateFriendRequest(friendRequest);
        if (!isUpdated) {
            throw new FriendRequestUpdateException("Failed to accept request between " + senderUsername + " and " + receiverUsername);
        }
        
        return true;
    }
    
    @Override
    public boolean deleteRequest(String senderUsername, String receiverUsername) {
        Pair<User, User> users = checkUsersExist(senderUsername, receiverUsername);
        
        boolean isDeleted = deleteFriendRequestPort.deleteFriendRequest(senderUsername, receiverUsername);
        
        if (!isDeleted) {
            throw new FriendRequestDeletionException("Failed to delete friend request between " + senderUsername + " and " + receiverUsername);
        }
        
        return true;
    }
    
    @Override
    public boolean declineRequest(String senderUsername, String receiverUsername) {
        Pair<User, User> users = checkUsersExist(senderUsername, receiverUsername);
        
        FriendRequest friendRequest = lookupFriendRequestPort.lookupFriendRequest(senderUsername, receiverUsername);
        friendRequest.declineFriendRequest();
        
        boolean isDeclined = updateFriendRequestPort.updateFriendRequest(friendRequest);
        if (!isDeclined) {
            throw new FriendRequestUpdateException("Failed to decline request between " + senderUsername + " and " + receiverUsername);
        }
        
        return true;
    }
    
    @Override
    public FriendRequest sendRequest(String senderUsername, String receiverUsername) {
      
        Pair<User, User> users = checkUsersExist(senderUsername, receiverUsername);
        
        FriendRequest friendRequest = new FriendRequest(users.getLeft(), users.getRight(), Status.PENDING);
        
        FriendRequest createdRequest = createFriendRequestPort.createRequest(friendRequest);
        if (createdRequest == null) {
            throw new FriendRequestCreationException("Failed to create friend request between " + senderUsername + " and " + receiverUsername);
        }
        
        return createdRequest;
    }
    
    private Pair<User, User> checkUsersExist(String senderUsername, String receiverUsername) {
        User sender = findUserPort.findUser(senderUsername);
        if (sender == null) {
            throw new UserNotFoundException("Failed to find user: " + senderUsername);
        }
        
        User receiver = findUserPort.findUser(receiverUsername);
        if (receiver == null) {
            throw new UserNotFoundException("Failed to find user: " + receiverUsername);
        }
        
        return Pair.of(sender, receiver);
    }
    
}
