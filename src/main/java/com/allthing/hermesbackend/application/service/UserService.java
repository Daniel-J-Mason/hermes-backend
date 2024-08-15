package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.ServiceException;
import com.allthing.hermesbackend.application.exception.user.UserCreationException;
import com.allthing.hermesbackend.application.exception.user.UserDeletionException;
import com.allthing.hermesbackend.application.exception.user.UserUpdateException;
import com.allthing.hermesbackend.application.ports.incoming.user.CreateUserUseCase;
import com.allthing.hermesbackend.application.ports.incoming.user.DeleteUserUseCase;
import com.allthing.hermesbackend.application.ports.incoming.user.ListFriendsUseCase;
import com.allthing.hermesbackend.application.ports.incoming.user.UpdateUserUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.user.CreateUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.DeleteUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.ListFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.UpdateUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, DeleteUserUseCase, ListFriendsUseCase, UpdateUserUseCase {
    
    private final CreateUserPort createUserPort;
    private final DeleteUserPort deleteUserPort;
    private final ListFriendsPort listFriendsPort;
    private final UpdateUserPort updateUserPort;
    
    @Override
    public User createUser(User user) {
        User createdUser = createUserPort.createUser(user);
        
        if (createdUser == null) {
            throw new UserCreationException("Failed to create user.");
        }
        
        return createdUser;
    }
    
    @Override
    public boolean deleteUser(String username) {
        boolean isDeleted = deleteUserPort.deleteUser(username);
        if (!isDeleted) {
            throw new UserDeletionException("Failed to delete user: " + username);
        }
        return true;
    }
    
    @Override
    public List<User> listFriends(String username) {
        List<User> friends = listFriendsPort.listFriends(username);
        if (friends == null) {
            throw new ServiceException("Failed to fetch friends for user: " + username);
        }
        
        return friends;
    }
    
    @Override
    public User updateUser(User user) {
        User updatedUser = updateUserPort.updateUser(user);
        
        if (updatedUser == null) {
            throw new UserUpdateException("Failed to update user: " + user.username());
        }
        
        return updatedUser;
    }
}
