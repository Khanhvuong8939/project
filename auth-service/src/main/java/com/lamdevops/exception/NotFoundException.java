package com.lamdevops.exception;

import com.lamdevops.backend.domain.Message;
import com.lamdevops.enums.MessageEnum;

public class NotFoundException extends BaseException{

    public NotFoundException(Message message) {
        super(message);
    }
}
