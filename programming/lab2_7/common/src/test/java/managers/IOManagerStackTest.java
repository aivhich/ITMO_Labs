package managers;

import org.ivanrevich.exceptions.AppException;
import org.ivanrevich.managers.IOManager;
import org.ivanrevich.managers.IOManagerStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Predicate;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("IOManagerStack Tests")
class IOManagerStackTest {

    /** Minimal stub IOManager for testing */
    private static IOManager stubIO(String filePath) {
        return new IOManager() {
            public String getFile() { return filePath; }
            public String read() { return ""; }
            public void write(String text) {}
            public String askString(String text) { return ""; }
            public Long askLong(String text) { return 0L; }
            public Double askDouble(String text) { return 0.0; }
            public Float askFloat(String text) { return 0f; }
            public Integer askInt(String text) { return 0; }
            public <T> T askValue(T init, Supplier<T> input, Predicate<T> validator) { return init; }
        };
    }

    private IOManager base;
    private IOManagerStack stack;

    @BeforeEach
    void setUp() {
        base = stubIO("IO");
        stack = new IOManagerStack(base);
    }

    @Test
    @DisplayName("current() after construction returns base")
    void currentIsBase() {
        assertSame(base, stack.current());
    }

    @Test
    @DisplayName("push() changes current to pushed IO")
    void pushChangesCurrent() {
        IOManager second = stubIO("/path/script.txt");
        stack.push(second);
        assertSame(second, stack.current());
    }

    @Test
    @DisplayName("pop() restores previous IO")
    void popRestoresPrevious() {
        IOManager second = stubIO("/path/script.txt");
        stack.push(second);
        stack.pop();
        assertSame(base, stack.current());
    }

    @Test
    @DisplayName("pop() on single element does nothing (base stays)")
    void popOnBaseDoesNothing() {
        stack.pop(); // should not throw or remove base
        assertSame(base, stack.current());
    }

    @Test
    @DisplayName("push() same file path throws AppException (recursion guard)")
    void pushDuplicateFileThrows() {
        IOManager duplicate = stubIO("IO"); // same file as base
        assertThrows(AppException.class, () -> stack.push(duplicate));
    }

    @Test
    @DisplayName("push() different files stacks correctly")
    void pushDifferentFilesStack() {
        IOManager s1 = stubIO("script1.txt");
        IOManager s2 = stubIO("script2.txt");
        stack.push(s1);
        stack.push(s2);
        assertSame(s2, stack.current());
        stack.pop();
        assertSame(s1, stack.current());
    }
}
