package com.wire.picklejar.execution.exception;

public class StepNotExecutableException extends ExecutionException{

    public StepNotExecutableException() {
    }

    public StepNotExecutableException(String message) {
        super(message);
    }

    public StepNotExecutableException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepNotExecutableException(Throwable cause) {
        super(cause);
    }
    
}
