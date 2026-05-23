package org.ivanrevich.utils;

import org.ivanrevich.requests.Request;

import java.io.IOException;

public class RequestDeserializer {
    public static Request deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        return (new Deserializer<Request>()).deserialize(bytes);
    }
}
