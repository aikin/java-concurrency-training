package com.concurrency.futuretask;

public class DataLoadException extends Exception {
    public DataLoadException(String message) {
        super(message);
    }

    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    private DataLoadException(Throwable cause) {
        super(cause);
    }

    private DataLoadException() {
        super();
    }
}
