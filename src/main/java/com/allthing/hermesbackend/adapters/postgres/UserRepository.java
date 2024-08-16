package com.allthing.hermesbackend.adapters.postgres;

import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.ports.outgoing.user.CreateUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.DeleteUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.ListFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.user.UpdateUserPort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements CreateUserPort, DeleteUserPort, FindUserPort, ListFriendsPort, UpdateUserPort {
    
    @Override
    public User createUser(User user) {
        return null;
    }
    
    @Override
    public boolean deleteUser(String username) {
        return false;
    }
    
    @Override
    public User findUser(String username) {
        return null;
    }
    
    @Override
    public List<User> listFriends(String username) {
        return List.of();
    }
    
    @Override
    public User updateUser(User user) {
        return null;
    }
}
