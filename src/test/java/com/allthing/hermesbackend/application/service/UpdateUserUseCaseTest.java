package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.user.UserUpdateException;
import com.allthing.hermesbackend.application.ports.incoming.user.UpdateUserUseCase;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateUserUseCaseTest {
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
    
    private UpdateUserUseCase underTest;
    
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
    public void updateUser_Success() {
        User user = new User("username", null, null);
        User updatedUser = new User("username", null, null);
        
        when(findUserPort.findUser(user.username())).thenReturn(user);
        when(updateUserPort.updateUser(user)).thenReturn(updatedUser);
        
        User returnedUser = underTest.updateUser(user);
        
        assertNotNull(returnedUser);
        assertEquals(updatedUser, returnedUser);
    }
    
    @Test
    public void updateUser_Failure_UserUpdateException() {
        User user = new User("username", null, null);
        
        when(findUserPort.findUser(user.username())).thenReturn(user);
        when(updateUserPort.updateUser(user)).thenReturn(null);
        
        assertThrows(UserUpdateException.class, () -> underTest.updateUser(user));
    }
    
}
