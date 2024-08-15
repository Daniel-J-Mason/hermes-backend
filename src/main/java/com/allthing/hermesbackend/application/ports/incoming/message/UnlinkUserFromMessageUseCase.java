package com.allthing.hermesbackend.application.ports.incoming.message;

import java.util.UUID;

public interface UnlinkUserFromMessageUseCase {
    boolean unLinkUser(UUID publicMessageId, String username);
}
