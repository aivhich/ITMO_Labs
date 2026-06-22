package responses;

import org.ivanrevich.responses.Response;
import org.ivanrevich.utils.ResultCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Response Tests")
class ResponseTest {

    @Test
    @DisplayName("constructor sets all fields")
    void constructorSetsFields() {
        Response<String> r = new Response<>(ResultCode.SUCCESS, "OK", "payload");
        assertEquals(ResultCode.SUCCESS, r.getResultCode());
        assertEquals("OK", r.getMessage());
        assertEquals("payload", r.getBody());
    }

    @Test
    @DisplayName("null body is allowed")
    void nullBody() {
        Response<Object> r = new Response<>(ResultCode.FAIL_0, "fail", null);
        assertNull(r.getBody());
    }

    @Test
    @DisplayName("integer body is returned correctly")
    void integerBody() {
        Response<Integer> r = new Response<>(ResultCode.SUCCESS, "count", 42);
        assertEquals(42, r.getBody());
    }
}
