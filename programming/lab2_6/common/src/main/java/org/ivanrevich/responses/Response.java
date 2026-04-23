package org.ivanrevich.responses;

import org.ivanrevich.utils.ResultCode;

import java.io.Serializable;
import java.util.List;

public class Response implements Serializable {
    private ResultCode resultCode;

    List<Serializable> objs;

    public Response(ResultCode resultCode, List<Serializable> objs) {
        this.resultCode = resultCode;
        this.objs = objs;
    }
}
