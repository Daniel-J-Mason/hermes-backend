package com.allthing.hermesbackend.application.domain;

public class Friend {
    private User sender;
    private User receiver;
    private Status status;
    
    public Friend(User sender, User receiver, Status status) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = status;
    }
    
    public void acceptFriendRequest() {
        if (status == Status.PENDING) {
            status = Status.ACCEPTED;
        } else {
            throw new RuntimeException("Cannot accept non-pending friend request");
        }
    }
}
