package com.lamdevops.enums;

public enum MessageEnum {

    //SUCCESS
    USER_UPDATED_SUCCESSFULLY("200","Updated user info successfully"),
    USER_CREATED_SUCCESSFULLY("200","Created new user successfully"),


    //ERROR
    USER_EMAIL_INVALID("400","Email {0} is not valid"),
    USER_USERNAME_INVALID("400","Username {0} is not valid"),
    USER_USERNAME_NOT_FOUND("400","Username {0} was not found"),
    USER_PASSWORD_NOT_EMPTY("400","Password must be not empty"),
    USER_PASSWORD_HAS_BEEN_USED("400","This password has been used in {0}"),
    USER_ID_NOT_FOUND("404","User ID {0} was not found"),
    USER_UPDATE_ERROR("400","Can not update user info, have some errors when updated info of {0}"),
    USER_CREATE_ERROR("400","Can not create new user, have some errors. Please try again"),
    USER_EMAIL_NOT_FOUND("404","User email {0} was not found"),
    MODEL_CONSTRAINT_EXCEPTION("400", "Validator constraint of {0} get exception");



    private String status;
    private String message;

    MessageEnum(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
