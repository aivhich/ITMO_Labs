package org.ivanrevich.commands;

public class Result {
    private org.ivanrevich.utils.ResultCode resultCode;
    private String message;

    public Result(org.ivanrevich.utils.ResultCode resultCode, String message) {
        this.resultCode = resultCode;
        this.message = message;
    }

    public org.ivanrevich.utils.ResultCode getResultCode() {
        return resultCode;
    }

    public String getMessage() {
        return message;
    }

}
