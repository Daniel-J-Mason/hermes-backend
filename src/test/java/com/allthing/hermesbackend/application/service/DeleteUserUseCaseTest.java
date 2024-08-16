package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.user.UserDeletionException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.user.DeleteUserUseCase;
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

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteUserUseCaseTest {
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
    
    private DeleteUserUseCase underTest;
    
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
    public void deleteUserSuccess() {
        String username = "username";
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(deleteUserPort.deleteUser(username)).thenReturn(true);
        
        assertTrue(underTest.deleteUser(username));
    }
    
    @Test
    public void deleteUserFailure() {
        String username = "username";
        User mockUser = new User(username, null, null);
        
        when(findUserPort.findUser(username)).thenReturn(mockUser);
        when(deleteUserPort.deleteUser(username)).thenReturn(false);
        
        assertThrows(UserDeletionException.class, () -> underTest.deleteUser(username));
    }
    
    @Test
    public void deleteUserFailureWhenUserNotExists() {
        String username = "username";
        
        when(findUserPort.findUser(username)).thenReturn(null);
        
        assertThrows(UserNotFoundException.class, () -> underTest.deleteUser(username));
    }
}
