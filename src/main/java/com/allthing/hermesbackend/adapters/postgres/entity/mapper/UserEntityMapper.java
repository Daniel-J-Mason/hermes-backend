package com.allthing.hermesbackend.adapters.postgres.entity.mapper;

import com.allthing.hermesbackend.adapters.postgres.entity.UserEntity;
import com.allthing.hermesbackend.application.domain.Email;
import com.allthing.hermesbackend.application.domain.PhoneNumber;
import com.allthing.hermesbackend.application.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityMapper {
    public UserEntity map(User user) {
        return UserEntity.builder()
                .username(user.username())
                .phoneNumber(String.valueOf(user.phoneNumber()))
                .email(String.valueOf(user.email()))
                .build();
    }
    
    public User map(UserEntity userEntity) {
        return new User(
                userEntity.getUsername(),
                new PhoneNumber(userEntity.getPhoneNumber()),
                new Email(userEntity.getEmail())
        );
    }
}
