package com.lamdevops.backend.domain;

import com.lamdevops.enums.MessageEnum;

import java.io.Serializable;
import java.text.MessageFormat;

/**
 * Created by greenlucky on 12/21/16.
 */
public class Message implements Serializable {

    /** The Serial Version UID for Serializable classes */
    private static final long serialVersionUID = 1L;

    private String status;
    private String message;
    private Object data;


    public Message(MessageEnum message) {
        this.status = message.getStatus();
        this.message = message.getMessage();
    }

    public Message(MessageEnum message, String... params) {
        this.status = message.getStatus();
        this.message = MessageFormat.format(message.getMessage(), params);
    }

    public Message(MessageEnum message, Object data, String... params) {
        this.status = message.getStatus();
        this.message = MessageFormat.format(message.getMessage(), params);
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class Builder {
        private String status;
        private MessageEnum message;
        private Object data;
        private String[] params;

        public Builder setStatus(String status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(MessageEnum message) {
            this.message = message;
            return this;
        }

        public Builder setData(Object data) {
            this.data = data;
            return this;
        }

        public Builder setParams(String... params) {
            this.params = params;
            return this;
        }

        public Message create() {
            return new Message(message, data, params);
        }


    }

}
