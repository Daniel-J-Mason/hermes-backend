package com.allthing.hermesbackend.application.ports.incoming.user;

import com.allthing.hermesbackend.application.domain.User;

public interface CreateUserUseCase {
    boolean createUser(User user);
}
