package requests;

import org.ivanrevich.auth.Credentials;
import org.ivanrevich.models.Vehicle;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Request Tests")
class RequestTest {

    @Test
    @DisplayName("commandType and args are set by constructor")
    void constructorSetsFields() {
        Request<String> req = new Request<>(CommandType.SHOW, "someArg");
        assertEquals(CommandType.SHOW, req.getCommandType());
        assertEquals("someArg", req.getArgs());
    }

    @Test
    @DisplayName("credentials are null initially")
    void credentialsNullByDefault() {
        Request<Void> req = new Request<>(CommandType.INFO, null);
        assertNull(req.getCredentials());
    }

    @Test
    @DisplayName("setCredentials stores credentials")
    void setCredentials() {
        Request<Void> req = new Request<>(CommandType.HELP, null);
        Credentials cred = new Credentials("user", "pass");
        req.setCredentials(cred);
        assertSame(cred, req.getCredentials());
        assertEquals("user", req.getCredentials().getUsername());
        assertEquals("pass", req.getCredentials().getPassword());
    }

    @Test
    @DisplayName("null args allowed")
    void nullArgs() {
        Request<Vehicle> req = new Request<>(CommandType.CLEAR, null);
        assertNull(req.getArgs());
    }

    @Test
    @DisplayName("all CommandType values can be set")
    void allCommandTypes() {
        for (CommandType ct : CommandType.values()) {
            Request<Void> req = new Request<>(ct, null);
            assertEquals(ct, req.getCommandType());
        }
    }
}
