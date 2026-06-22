package managers;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.auth.Credentials;
import org.ivanrevich.auth.UserDTO;
import org.ivanrevich.managers.AuthManagerImpl;
import org.ivanrevich.network.Client;
import org.ivanrevich.requests.CommandType;
import org.ivanrevich.requests.Request;
import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client AuthManagerImpl Tests")
class AuthManagerImplTest {

    @Mock
    Client client;

    ManagersLocator locator;
    AuthManagerImpl authManager;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        authManager = new AuthManagerImpl(locator);
    }

    @Test
    @DisplayName("authenticate() with SUCCESS response stores credentials and userId, returns true")
    void authenticateSuccessStoresState() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", 42);
        when(client.sendObject(any())).thenReturn(r);

        boolean result = authManager.authenticate("alice", "secret");

        assertTrue(result);
        assertEquals(42, authManager.authorizedUserId());
        assertEquals("alice", authManager.getCredentials().getUsername());
        assertEquals("secret", authManager.getCredentials().getPassword());
    }

    @Test
    @DisplayName("authenticate() sends LOGIN request with correct credentials")
    void authenticateSendsCorrectRequest() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", 1);
        when(client.sendObject(any())).thenReturn(r);

        authManager.authenticate("bob", "pw");

        ArgumentCaptor<Request<Credentials>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.LOGIN, captor.getValue().getCommandType());
        assertEquals("bob", captor.getValue().getArgs().getUsername());
    }

    @Test
    @DisplayName("authenticate() with non-SUCCESS response returns false and does not store state")
    void authenticateFailureDoesNotStoreState() throws Exception {
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.INVALID_PASSWORD, "bad", null));

        boolean result = authManager.authenticate("alice", "wrong");

        assertFalse(result);
        assertNull(authManager.getCredentials());
        assertNull(authManager.authorizedUserId());
    }

    @Test
    @DisplayName("authenticate() returns false on IOException")
    void authenticateIoExceptionReturnsFalse() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        assertFalse(authManager.authenticate("alice", "secret"));
    }

    @Test
    @DisplayName("register() returns true when response is SUCCESS with a UserDTO body")
    void registerSuccessReturnsTrue() throws Exception {
        Response r =
                new Response<>(ResultCode.SUCCESS, "ok", new UserDTO(1, "newuser"));
        when(client.sendObject(any())).thenReturn(r);

        boolean result = authManager.register("newuser", "pw");

        assertTrue(result);
    }

    @Test
    @DisplayName("register() returns false when response is not SUCCESS")
    void registerFailureReturnsFalse() throws Exception {
        when(client.sendObject(any())).thenReturn(
                new Response<>(ResultCode.INVALID_USERNAME, "taken", null));

        assertFalse(authManager.register("taken", "pw"));
    }

    @Test
    @DisplayName("register() returns false on IOException")
    void registerIoExceptionReturnsFalse() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());

        assertFalse(authManager.register("user", "pw"));
    }

    @Test
    @DisplayName("logout() clears credentials and userId, returns true")
    void logoutClearsState() throws Exception {
        Response r = new Response<>(ResultCode.SUCCESS, "ok", 5);
        when(client.sendObject(any())).thenReturn(r);
        authManager.authenticate("alice", "secret"); // populate state first

        boolean result = authManager.logout();

        assertTrue(result);
        assertNull(authManager.getCredentials());
        assertNull(authManager.authorizedUserId());
    }
}
