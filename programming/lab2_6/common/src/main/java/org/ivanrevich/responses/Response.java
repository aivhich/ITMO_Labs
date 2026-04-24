package org.ivanrevich.responses;

import org.ivanrevich.utils.ResultCode;

import java.io.Serializable;
import java.util.List;

public class Response<T> implements Serializable {
    private ResultCode resultCode;

    private String message;

    private T body;

    public Response(ResultCode resultCode, String message,  T body) {
        this.resultCode = resultCode;
        this.message = message;
        this.body = body;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

    public T getBody() {
        return body;
    }
}
