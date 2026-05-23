package org.ivanrevich.responses;

import org.ivanrevich.utils.ResultCode;


public class Result<T> {
    private org.ivanrevich.utils.ResultCode resultCode;
    private String message;
    private T output;

    public Result(ResultCode resultCode, String message, T output) {
        this.resultCode = resultCode;
        this.message = message;
        this.output = output;
    }

    public org.ivanrevich.utils.ResultCode getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public T getOutput() {
        return output;
    }
}