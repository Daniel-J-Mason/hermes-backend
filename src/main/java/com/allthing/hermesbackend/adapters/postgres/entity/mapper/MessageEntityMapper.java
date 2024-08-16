package com.allthing.hermesbackend.adapters.postgres.entity.mapper;

import com.allthing.hermesbackend.adapters.postgres.entity.MessageEntity;
import com.allthing.hermesbackend.application.domain.Message;
import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

import java.text.ParseException;

@Component
@RequiredArgsConstructor
public class MessageEntityMapper {
    
    private final UserEntityMapper userEntityMapper;
    
    public MessageEntity map(Message message) {
        return MessageEntity.builder()
                .publicId(message.publicId())
                .title(message.title())
                .body(message.body())
                .scheduledTime(message.scheduledTime().getCronExpression())
                .creator(userEntityMapper.map(message.creator()))
                .build();
    }
    
    public Message map(MessageEntity messageEntity) {
        CronExpression scheduledTime = null;
        try {
            scheduledTime = new CronExpression(messageEntity.getScheduledTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        
        return new Message(
                messageEntity.getPublicId(),
                messageEntity.getTitle(),
                messageEntity.getBody(),
                scheduledTime,
                userEntityMapper.map(messageEntity.getCreator())
        );
    }
}
