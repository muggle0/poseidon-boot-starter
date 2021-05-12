package com.muggle.poseidon.base.exception;

/**
 * Description
 * Date 2021/5/12
 * Created by muggle
 */
public class EditLockException extends RuntimeException {
    public EditLockException() {
    }

    public EditLockException(String message) {
        super(message);
    }

    public EditLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public EditLockException(Throwable cause) {
        super(cause);
    }

    public EditLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
