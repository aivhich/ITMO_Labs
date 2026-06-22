package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.Login;
import org.ivanrevich.commands.Logout;
import org.ivanrevich.commands.Signup;
import org.ivanrevich.managers.AuthManager;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Login / Signup / Logout Command Tests")
class LoginSignupLogoutCommandTest {

    @Mock
    AuthManager authManager;

    ManagersLocator locator;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(AuthManager.class, authManager);
    }

    //  Login 

    @Test
    @DisplayName("Login with wrong arg count returns INVALID_REQUEST without calling AuthManager")
    void loginWrongArgCount() {
        Login login = new Login(locator);
        assertEquals(ResultCode.INVALID_REQUEST, login.run(new String[]{"onlyone"}));
        verifyNoInteractions(authManager);
    }

    @Test
    @DisplayName("Login delegates to AuthManager.authenticate and returns SUCCESS on true")
    void loginSuccessDelegates() {
        when(authManager.authenticate("alice", "secret")).thenReturn(true);

        Login login = new Login(locator);
        ResultCode result = login.run(new String[]{"alice", "secret"});

        assertEquals(ResultCode.SUCCESS, result);
        verify(authManager).authenticate("alice", "secret");
    }

    @Test
    @DisplayName("Login returns INVALID_REQUEST when AuthManager.authenticate returns false")
    void loginFailureReturnsInvalidRequest() {
        when(authManager.authenticate("alice", "wrong")).thenReturn(false);

        Login login = new Login(locator);
        assertEquals(ResultCode.INVALID_REQUEST, login.run(new String[]{"alice", "wrong"}));
    }

    //  Signup 

    @Test
    @DisplayName("Signup with wrong arg count returns INVALID_REQUEST without calling AuthManager")
    void signupWrongArgCount() {
        Signup signup = new Signup(locator);
        assertEquals(ResultCode.INVALID_REQUEST, signup.run(new String[]{}));
        verifyNoInteractions(authManager);
    }

    @Test
    @DisplayName("Signup delegates to AuthManager.register and returns SUCCESS on true")
    void signupSuccessDelegates() {
        when(authManager.register("bob", "pw")).thenReturn(true);

        Signup signup = new Signup(locator);
        ResultCode result = signup.run(new String[]{"bob", "pw"});

        assertEquals(ResultCode.SUCCESS, result);
        verify(authManager).register("bob", "pw");
    }

    @Test
    @DisplayName("Signup returns INVALID_REQUEST when AuthManager.register returns false")
    void signupFailureReturnsInvalidRequest() {
        when(authManager.register("taken", "pw")).thenReturn(false);

        Signup signup = new Signup(locator);
        assertEquals(ResultCode.INVALID_REQUEST, signup.run(new String[]{"taken", "pw"}));
    }

    //  Logout 

    @Test
    @DisplayName("Logout delegates to AuthManager.logout and returns SUCCESS on true")
    void logoutSuccessDelegates() {
        when(authManager.logout()).thenReturn(true);

        Logout logout = new Logout(locator);
        ResultCode result = logout.run(new String[]{});

        assertEquals(ResultCode.SUCCESS, result);
        verify(authManager).logout();
    }

    @Test
    @DisplayName("Logout returns INVALID_REQUEST when AuthManager.logout returns false")
    void logoutFailureReturnsInvalidRequest() {
        when(authManager.logout()).thenReturn(false);

        Logout logout = new Logout(locator);
        assertEquals(ResultCode.INVALID_REQUEST, logout.run(new String[]{}));
    }
}
