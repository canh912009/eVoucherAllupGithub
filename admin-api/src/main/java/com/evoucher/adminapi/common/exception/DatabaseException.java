package com.evoucher.adminapi.common.exception;

public class DatabaseException extends RuntimeException{
    public DatabaseException(String msg, Exception e) {
        super(msg, e);
    }
}
