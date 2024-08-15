package com.allthing.hermesbackend.application.ports.outgoing.user;

import com.allthing.hermesbackend.application.domain.User;

public interface DeleteUserPort {
    User deleteUser(String username);
}
