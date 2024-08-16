package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.user.UserCreationException;
import com.allthing.hermesbackend.application.ports.incoming.user.CreateUserUseCase;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateUserUseCaseTest {
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
    
    private CreateUserUseCase underTest;
    
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
    public void createUserSuccess() {
        User user = new User("username", null, null);
        when(createUserPort.createUser(user)).thenReturn(user);
        
        User createdUser = underTest.createUser(user);
        
        assertNotNull(createdUser);
    }
    
    @Test
    public void createUserFailure() {
        User user = new User("username", null, null);
        when(createUserPort.createUser(user)).thenReturn(null);
        
        assertThrows(UserCreationException.class, () -> underTest.createUser(user));
    }
}
