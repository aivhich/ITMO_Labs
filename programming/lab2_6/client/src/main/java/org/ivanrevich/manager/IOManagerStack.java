package org.ivanrevich.manager;

import org.ivanrevich.exceptions.Exceptions;

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
 * @version 1.0
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
        if(!stack.stream().filter(value -> value.getFile().equals(io.getFile()) ).findFirst().isEmpty()) {
            throw new RuntimeException(Exceptions.RECURRENT_SCRIPT_ERROR);
        }
        stack.push(io);
    }

    public void pop() {
        if(stack.size() > 1) {
            stack.pop();
        }
    }
}
