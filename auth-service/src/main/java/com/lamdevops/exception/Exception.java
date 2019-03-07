package com.lamdevops.exception;

import com.lamdevops.backend.domain.Message;

public class Exception extends BaseException{

    public Exception(Message message) {
        super(message);
    }
}
