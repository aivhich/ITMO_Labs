package org.ivanrevich.managers;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.utils.ResultCode;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Стек менеджеров ввода-вывода.
 * <p>
 * Позволяет переключаться между источниками ввода (консоль/файл)
 * с возможностью возврата к предыдущему источнику.
 * </p>
 *
 * @author Ivan Prokhorevich
 * @version 2.0
 * @see IOManager
 */
public class IOManagerStack {
    private final Deque<IOManager> stack = new ArrayDeque<>();

    public IOManagerStack(IOManager base) {
        stack.push(base);
    }

    public IOManager current() {
        return stack.peek();
    }

    public void push(IOManager io) {
        boolean alreadyInStack = stack.stream()
                .anyMatch(existing -> existing.getFile().equals(io.getFile()));

        if (alreadyInStack) {
            throw new AppException(ResultCode.RECURRENT_SCRIPT_ERROR);
        }
        stack.push(io);
    }

    public void pop() {
        if (stack.size() > 1) {
            stack.pop();
        }
    }
}