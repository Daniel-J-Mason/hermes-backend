package com.allthing.hermesbackend.application.service;

import com.allthing.hermesbackend.application.domain.Message;
import com.allthing.hermesbackend.application.domain.User;
import com.allthing.hermesbackend.application.exception.message.MessageCreationException;
import com.allthing.hermesbackend.application.exception.message.MessageLinkException;
import com.allthing.hermesbackend.application.exception.message.MessageUpdateException;
import com.allthing.hermesbackend.application.exception.ServiceException;
import com.allthing.hermesbackend.application.exception.user.UserNotFoundException;
import com.allthing.hermesbackend.application.ports.incoming.message.CreateMessageUseCase;
import com.allthing.hermesbackend.application.ports.incoming.message.DeleteMessageUseCase;
import com.allthing.hermesbackend.application.ports.incoming.message.LinkUserToMessageUseCase;
import com.allthing.hermesbackend.application.ports.incoming.message.ListUserMessagesUseCase;
import com.allthing.hermesbackend.application.ports.incoming.message.UnlinkUserFromMessageUseCase;
import com.allthing.hermesbackend.application.ports.incoming.message.UpdateMessageUseCase;
import com.allthing.hermesbackend.application.ports.outgoing.friend.AreFriendsPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageLinksPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CheckMessageOwnershipPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.CreateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessageLinkPort;
import com.allthing.hermesbackend.application.ports.outgoing.message.DeleteMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.GetMessagesByUsernamePort;
import com.allthing.hermesbackend.application.ports.outgoing.message.UpdateMessagePort;
import com.allthing.hermesbackend.application.ports.outgoing.user.FindUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

//TODO implement quartz scheduling on message creation and update for scheduled tasks, set up service to actually send out texts
@Service
@RequiredArgsConstructor
public class MessagesService implements CreateMessageUseCase, DeleteMessageUseCase, LinkUserToMessageUseCase, ListUserMessagesUseCase, UnlinkUserFromMessageUseCase, UpdateMessageUseCase {
    
    private final CheckMessageLinksPort checkMessageLinksPort;
    private final CheckMessageOwnershipPort checkMessageOwnershipPort;
    private final CreateMessageLinkPort createMessageLinkPort;
    private final CreateMessagePort createMessagePort;
    private final DeleteMessagePort deleteMessagePort;
    private final DeleteMessageLinkPort deleteMessageLinkPort;
    private final GetMessagesByUsernamePort getMessagesByUsernamePort;
    private final UpdateMessagePort updateMessagePort;
    private final AreFriendsPort areFriendsPort;
    private final FindUserPort findUserPort;
    
    // Create a message tied to creating user
    @Override
    public Message createMessage(String username, Message message) {
        checkUserExists(username);
        
        Message createdMessage = createMessagePort.createMessage(username, message);
        if (createdMessage == null) {
            throw new MessageCreationException("Failed to create message for user: " + username);
        }
        return createMessagePort.createMessage(username, message);
    }
    
    // Delete a message (user must own message)
    @Override
    public boolean deleteMessage(String username, UUID publicMessageId) {
        checkUserExists(username);
        
        if (!checkMessageOwnershipPort.userOwnsMessage(username, publicMessageId)) {
            throw new MessageCreationException("User does not own this message. Deletion failed.");
        }
        
        return deleteMessagePort.deleteMessage(username, publicMessageId);
    }
    
    // If user owns message, and is friends with other user, link other user to message
    @Override
    public boolean linkUser(String sendingUsername, String receivingUsername, UUID publicMessageId) {
        checkUserExists(sendingUsername);
        checkUserExists(receivingUsername);
        
        if (!areFriendsPort.areFriends(sendingUsername, receivingUsername) || !checkMessageOwnershipPort.userOwnsMessage(sendingUsername, publicMessageId)) {
            throw new MessageLinkException("Message linking failed. Ensure both users are friends and message is owned by the sender");
        }
        return createMessageLinkPort.createLink(publicMessageId, receivingUsername);
    }
    
    // return users messages
    @Override
    public List<Message> getMessages(String username) {
        checkUserExists(username);
        
        List<Message> messages = getMessagesByUsernamePort.getMessages(username);
        if (messages == null) {
            throw new ServiceException("Failed to fetch messages for user: " + username);
        }
        
        return messages;
    }
    
    // If user is not the owner and is linked to a message, allow them to remove their connection to it
    @Override
    public boolean unLinkUser(UUID publicMessageId, String username) {
        checkUserExists(username);
        
        if (checkMessageOwnershipPort.userOwnsMessage(username, publicMessageId)) {
            throw new ServiceException("Owner cannot unlink from message, consider deleting message instead.");
        }
        
        if (!checkMessageLinksPort.checkUserLinkedToMessage(username, publicMessageId)) {
            throw new MessageLinkException("This user is not linked to the message, therefore cannot be unlinked.");
        }
        
        return deleteMessageLinkPort.deleteLink(publicMessageId, username);
    }
    
    
    // if user owns or is linked to message, allow them to update message
    @Override
    public Message updateMessage(String username, Message message) {
        checkUserExists(username);
        
        if (!checkMessageLinksPort.checkUserLinkedToMessage(username, message.publicId())
            && (!checkMessageOwnershipPort.userOwnsMessage(username, message.publicId()))) {
            throw new MessageUpdateException("Message update failed. User neither owns nor is linked to message");
        }
        return updateMessagePort.updateMessage(username, message);
    }
    
    private void checkUserExists(String username) {
        User user = findUserPort.findUser(username);
        if (user == null) {
            throw new UserNotFoundException("User does not exist: " + username);
        }
    }

}
