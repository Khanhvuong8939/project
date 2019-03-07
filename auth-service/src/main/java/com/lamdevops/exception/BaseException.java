package com.lamdevops.exception;

import com.lamdevops.backend.domain.Message;

public class BaseException extends RuntimeException {

    private Message message;


    public BaseException(Message message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message.getMessage();
    }


    public Message getOriginalMessage() {
        return message;
    }

    public void setOriginalMessage(Message message) {
        this.message = message;
    }
}
