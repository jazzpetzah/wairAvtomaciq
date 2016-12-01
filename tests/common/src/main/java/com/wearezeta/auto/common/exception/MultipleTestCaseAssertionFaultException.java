package com.wearezeta.auto.common.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class MultipleTestCaseAssertionFaultException extends Exception {
    private List<AssertionError> exceptions;

    public MultipleTestCaseAssertionFaultException(List<AssertionError> exceptions) {
        this.exceptions = exceptions;
    }

    @Override
    public String getMessage() {
        return exceptions.stream().map(this::getExceptionErrorMessage).collect(Collectors.joining("\n"));
    }

    private String getExceptionErrorMessage(AssertionError e) {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("Assertion fault [");
        stringBuffer.append(e.getMessage());
        stringBuffer.append("]\n");
        stringBuffer.append("ROOT CAUSE:");

        try (
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer)
        ) {
            e.printStackTrace(printWriter);
            stringBuffer.append(writer);
        } catch (IOException ioe) {
            e.printStackTrace();
        }

        return stringBuffer.toString();
    }
}
