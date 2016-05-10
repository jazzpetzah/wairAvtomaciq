package com.wire.picklejar.execution.exception;

public class StepNotFoundException extends ExecutionException{

    public StepNotFoundException() {
    }

    public StepNotFoundException(String message) {
        super(message);
    }

    public StepNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public StepNotFoundException(Throwable cause) {
        super(cause);
    }
    
}
