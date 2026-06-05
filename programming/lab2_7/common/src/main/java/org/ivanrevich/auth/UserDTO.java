package org.ivanrevich.auth;

import java.io.Serializable;

public record UserDTO (
        int id,
        String username
) implements Serializable {
}
