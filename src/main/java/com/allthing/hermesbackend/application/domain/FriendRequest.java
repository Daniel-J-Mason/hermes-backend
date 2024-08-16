package com.allthing.hermesbackend.application.domain;

import com.allthing.hermesbackend.application.exception.friendrequest.IllegalStatusChangeException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FriendRequest {
    private User sender;
    private User receiver;
    private Status status;
    
    public FriendRequest(User sender, User receiver, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }
    
    public void acceptFriendRequest() {
        if (status == Status.PENDING) {
            status = Status.ACCEPTED;
        } else {
            throw new IllegalStatusChangeException("Cannot accept non-pending friend request");
        }
    }
    
    public void declineFriendRequest() {
        if (status == Status.PENDING) {
            status = Status.DECLINED;
        } else {
            throw new IllegalStatusChangeException("Cannot decline non-pending friend request");
        }
    }
}
