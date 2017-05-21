package com.staszp.exception;


public class TestAppException extends RuntimeException {

    private Integer errorCode;

    public TestAppException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
