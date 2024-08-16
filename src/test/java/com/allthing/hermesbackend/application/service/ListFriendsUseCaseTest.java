package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.ServiceException;
import com.allthing.hermesbackend.application.ports.incoming.user.ListFriendsUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.user.CreateUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.DeleteUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.ListFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.UpdateUserPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListFriendsUseCaseTest {
    @Mock
    private CreateUserPort createUserPort;
    @Mock
    private DeleteUserPort deleteUserPort;
    @Mock
    private ListFriendsPort listFriendsPort;
    @Mock
    private UpdateUserPort updateUserPort;
    @Mock
    private FindUserPort findUserPort;
    
    private ListFriendsUseCase underTest;
    
    @BeforeEach
    void setUp() {
        underTest = new UserService(
                createUserPort,
                deleteUserPort,
                listFriendsPort,
                updateUserPort,
                findUserPort
        );
    }
    
    @Test
    public void listFriendsSuccess() {
        String username = "username";
        User mockUser = new User(username, null, null);
        
        User mockUser1 = new User("username1", null, null);
        User mockUser2 = new User("username2", null, null);
        List<User> friends = Arrays.asList(mockUser1, mockUser2);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(listFriendsPort.listFriends(username)).thenReturn(friends);
        
        List<User> returnedFriends = underTest.listFriends(username);
        
        assertNotNull(returnedFriends);
        assertEquals(friends, returnedFriends);
    }
    
    @Test
    public void listFriendsFailure() {
        String username = "username";
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(listFriendsPort.listFriends(username)).thenReturn(null);
        
        assertThrows(ServiceException.class, () -> underTest.listFriends(username));
    }
    
}
