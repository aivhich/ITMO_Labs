package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.RemoveById;
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
@DisplayName("Client RemoveById Command Tests")
class RemoveByIdCommandTest {

    @Mock
    Client client;

    ManagersLocator locator;
    RemoveById removeById;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        removeById = new RemoveById(locator);
    }

    @Test
    @DisplayName("wrong number of args returns INVALID_NUM_OF_ARGS without contacting the server")
    void wrongArgCountReturnsInvalidNumOfArgs() {
        ResultCode result = removeById.run(new String[]{});
        assertEquals(ResultCode.INVALID_NUM_OF_ARGS, result);
        verifyNoInteractions(client);
    }

    @Test
    @DisplayName("non-numeric id returns INVALID_ARGS without contacting the server")
    void nonNumericIdReturnsInvalidArgs() {
        ResultCode result = removeById.run(new String[]{"abc"});
        assertEquals(ResultCode.INVALID_ARGS, result);
        verifyNoInteractions(client);
    }

    @Test
    @DisplayName("valid numeric id sends REMOVE_BY_ID request with parsed int")
    void validIdSendsCorrectRequest() throws Exception {
        when(client.sendObject(any())).thenReturn(new Response<>(ResultCode.SUCCESS, "ok", null));

        ResultCode result = removeById.run(new String[]{"42"});

        assertEquals(ResultCode.SUCCESS, result);

        ArgumentCaptor<Request<?>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.REMOVE_BY_ID, captor.getValue().getCommandType());
        assertEquals(42, captor.getValue().getArgs());
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, removeById.run(new String[]{"1"}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, removeById.run(new String[]{"1"}));
    }
}
