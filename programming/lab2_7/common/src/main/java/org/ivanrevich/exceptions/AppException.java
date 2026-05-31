package org.ivanrevich.exceptions;

import org.ivanrevich.utils.ResultCode;

/**
 * Типизированное исключение приложения.
 * <p>
 * Заменяет {@code RuntimeException} со строковыми константами из {@code Exceptions}.
 * Несёт {@link ResultCode} — это позволяет делать switch по enum в {@code ClientMain}
 * без проблемы "constant expression required".
 * </p>
 *
 * <pre>{@code
 * // Раньше:
 * throw new RuntimeException(Exceptions.SCRIPT_END);
 *
 * // Теперь:
 * throw new AppException(ResultCode.SCRIPT_END);
 * }</pre>
 *
 * @author Ivan Prokhorevich
 * @version 1.0
 */
public class AppException extends RuntimeException {
    private final ResultCode code;

    public AppException(ResultCode code) {
        super(code.getMessage());
        this.code = code;
    }

    public AppException(ResultCode code, String detail) {
        super(code.getMessage() + ": " + detail);
        this.code = code;
    }

    public AppException(ResultCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    public ResultCode getCode() {
        return code;
    }
}