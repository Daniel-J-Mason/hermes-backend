package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.FriendRequest;
import com.allthing.hermesbackend.application.domain.Status;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.friendrequest.FriendRequestUpdateException;
import com.allthing.hermesbackend.application.exception.friendrequest.IllegalStatusChangeException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.friend.AcceptFriendRequestUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.CreateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.DeleteFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.LookupFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.friend.UpdateFriendRequestPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AcceptFriendRequestUseCaseTest {
    
    @Mock
    private CreateFriendRequestPort createFriendRequestPort;
    @Mock
    private DeleteFriendRequestPort deleteFriendRequestPort;
    @Mock
    private UpdateFriendRequestPort updateFriendRequestPort;
    @Mock
    private LookupFriendRequestPort lookupFriendRequestPort;
    @Mock
    private FindUserPort findUserPort;
    
    private AcceptFriendRequestUseCase underTest;
    private User user1;
    private User user2;
    
    @BeforeEach
    void setUp() {
        underTest = new FriendRequestService(
                createFriendRequestPort,
                deleteFriendRequestPort,
                updateFriendRequestPort,
                lookupFriendRequestPort,
                findUserPort
        );
        
        user1 = new User("username1", null, null);
        user2 = new User("username2", null, null);
    }
    
    @Test
    void shouldSucceedIfBothUsersFoundAndUpdateSucceeds() {
        FriendRequest friendRequest = new FriendRequest(user1, user2, Status.PENDING);
        
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(lookupFriendRequestPort.lookupFriendRequest(user1.username(), user2.username())).thenReturn(friendRequest);
        when(updateFriendRequestPort.updateFriendRequest(any(FriendRequest.class))).thenReturn(true);
        
        assertTrue(underTest.acceptRequest(user1.username(), user2.username()));
    }
    
    @Test
    public void testAcceptRequestStatusChange() {
        FriendRequest friendRequest = new FriendRequest(user1, user2, Status.PENDING);
        
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(lookupFriendRequestPort.lookupFriendRequest(user1.username(), user2.username())).thenReturn(friendRequest);
        
        when(updateFriendRequestPort.updateFriendRequest(friendRequest)).thenAnswer(invocation -> {
            FriendRequest updatedRequest = invocation.getArgument(0);
            
            // Verify that the status of the friend request has changed to ACCEPTED
            assertEquals(Status.ACCEPTED, updatedRequest.getStatus());
            
            return true;
        });
        
        underTest.acceptRequest(user1.username(), user2.username());
    }
    
    @Test
    void shouldThrowExceptionIfSenderNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.acceptRequest(user1.username(), user2.username()));
    }
    
    @Test
    void shouldThrowExceptionIfReceiverNotFound() {
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.acceptRequest(user1.username(), user2.username()));
    }
    
    @Test
    void shouldThrowExceptionIfUpdateFails() {
        FriendRequest friendRequest = new FriendRequest(user1, user2, Status.PENDING);
        
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(lookupFriendRequestPort.lookupFriendRequest(user1.username(), user2.username())).thenReturn(friendRequest);
        when(updateFriendRequestPort.updateFriendRequest(any(FriendRequest.class))).thenReturn(false);
        
        assertThrows(FriendRequestUpdateException.class, () -> underTest.acceptRequest(user1.username(), user2.username()));
    }
    
    @Test
    public void shouldThrowExceptionIfFriendRequestNotPending() {
        FriendRequest friendRequest = new FriendRequest(user1, user2, Status.ACCEPTED);
        
        when(findUserPort.findUser(user1.username())).thenReturn(user1);
        when(findUserPort.findUser(user2.username())).thenReturn(user2);
        when(lookupFriendRequestPort.lookupFriendRequest(user1.username(), user2.username())).thenReturn(friendRequest);
        
        // Validate that an exception is thrown when trying to accept a friend request with a status of ACCEPTED
        assertThrows(IllegalStatusChangeException.class, () -> underTest.acceptRequest(user1.username(), user2.username()));
    }
    
}