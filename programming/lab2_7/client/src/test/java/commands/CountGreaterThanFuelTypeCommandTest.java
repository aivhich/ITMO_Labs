package commands;

import org.ivanrevich.ManagersLocator;
import org.ivanrevich.commands.CountGreaterThanFuelType;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.models.FuelType;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client CountGreaterThanFuelType Command Tests")
class CountGreaterThanFuelTypeCommandTest {

    @Mock
    Client client;
    @Mock
    IOManager ioManager;

    ManagersLocator locator;
    CountGreaterThanFuelType command;

    @BeforeEach
    void setUp() {
        locator = new ManagersLocator();
        locator.register(Client.class, client);
        locator.register(IOManager.class, ioManager);
        command = new CountGreaterThanFuelType(locator);
    }

    @Test
    @DisplayName("wrong number of args returns INVALID_NUM_OF_ARGS without contacting server")
    void wrongArgCount() {
        assertEquals(ResultCode.INVALID_NUM_OF_ARGS, command.run(new String[]{}));
        verifyNoInteractions(client);
    }

    @Test
    @DisplayName("invalid fuel type name returns INVALID_ARGS without contacting server")
    void invalidFuelTypeName() {
        assertEquals(ResultCode.INVALID_ARGS, command.run(new String[]{"NOT_A_FUEL"}));
        verifyNoInteractions(client);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    @DisplayName("valid fuel type sends request and writes the count")
    void validFuelTypeSendsRequest() throws Exception {
        Response response = new Response<>(ResultCode.SUCCESS, "ok", 5L);
        when(client.sendObject(any())).thenReturn(response);

        ResultCode result = command.run(new String[]{"nuclear"});

        assertEquals(ResultCode.SUCCESS, result);
        verify(ioManager).write(anyString());

        ArgumentCaptor<Request<FuelType>> captor = ArgumentCaptor.forClass(Request.class);
        verify(client).sendObject(captor.capture());
        assertEquals(CommandType.COUNT_GREATER_THAN_FUEL_TYPE, captor.getValue().getCommandType());
        assertEquals(FuelType.NUCLEAR, captor.getValue().getArgs());
    }

    @Test
    @DisplayName("IOException maps to INTERNAL_CLI_ERROR")
    void ioExceptionMapsToInternalCliError() throws Exception {
        when(client.sendObject(any())).thenThrow(new java.io.IOException());
        assertEquals(ResultCode.INTERNAL_CLI_ERROR, command.run(new String[]{"KEROSENE"}));
    }

    @Test
    @DisplayName("ClassNotFoundException maps to INVALID_REQUEST")
    void classNotFoundMapsToInvalidRequest() throws Exception {
        when(client.sendObject(any())).thenThrow(new ClassNotFoundException());
        assertEquals(ResultCode.INVALID_REQUEST, command.run(new String[]{"KEROSENE"}));
    }
}
