package com.wire.picklejar.execution.exception;

public class StepNotExecutableException extends ExecutionException{
    
    private final long executionTime;

    public StepNotExecutableException(long executionTime, String message) {
        super(message);
        this.executionTime = executionTime;
    }

    public StepNotExecutableException(long executionTime, String message, Throwable cause) {
        super(message, cause);
        this.executionTime = executionTime;
    }

    public StepNotExecutableException(long executionTime, Throwable cause) {
        super(cause);
        this.executionTime = executionTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }
    
}
