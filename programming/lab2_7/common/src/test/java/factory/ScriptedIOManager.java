package factory;

import org.ivanrevich.managers.IOManager;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A scripted IOManager test double.
 * <p>
 * Feed it a sequence of string answers via the constructor; each call to
 * read()/askString() pops the next answer off the queue. askInt/askFloat/etc.
 * parse the popped string the same way the production read() pipeline would.
 * write() calls are recorded for inspection.
 * </p>
 */
public class ScriptedIOManager implements IOManager {
    private final Deque<String> answers;
    private final java.util.List<String> writtenLines = new java.util.ArrayList<>();

    public ScriptedIOManager(String... answers) {
        this.answers = new ArrayDeque<>(Arrays.asList(answers));
    }

    public ScriptedIOManager(List<String> answers) {
        this.answers = new ArrayDeque<>(answers);
    }

    public List<String> getWrittenLines() {
        return writtenLines;
    }

    private String nextAnswer() {
        if (answers.isEmpty()) {
            throw new IllegalStateException("ScriptedIOManager ran out of scripted answers");
        }
        return answers.poll();
    }

    @Override
    public String getFile() {
        return "SCRIPTED";
    }

    @Override
    public String read() {
        return nextAnswer();
    }

    @Override
    public void write(String text) {
        writtenLines.add(text);
    }

    @Override
    public String askString(String text) {
        write(text);
        return read();
    }

    @Override
    public Long askLong(String text) {
        write(text);
        String s = read();
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid long");
        }
    }

    @Override
    public Double askDouble(String text) {
        write(text);
        String s = read().replace(',', '.');
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid double");
        }
    }

    @Override
    public Float askFloat(String text) {
        write(text);
        String s = read().replace(',', '.');
        try {
            return Float.parseFloat(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid float");
        }
    }

    @Override
    public Integer askInt(String text) {
        write(text);
        String s = read();
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            if (s.isBlank()) return null;
            throw new RuntimeException("Value is not a valid integer");
        }
    }

    /**
     * Mirrors the retry-loop behaviour of IOManagerImpl.askValue: tries up to
     * 3 times, accepts the first value that satisfies the validator.
     */
    @Override
    public <T> T askValue(T initValue, Supplier<T> input, Predicate<T> validator) {
        boolean isUpdateMode = initValue != null;
        T value = initValue;

        for (int i = 0; i < 3; i++) {
            try {
                T newValue = input.get();
                if (validator.test(newValue)) {
                    if (isUpdateMode && (newValue == null || newValue.toString().isEmpty())) return value;
                    value = newValue;
                    break;
                }
                if (isUpdateMode && (newValue == null || newValue.toString().isEmpty())) return value;
            } catch (RuntimeException e) {
                if (i == 2) throw e;
            }
        }
        return value;
    }
}
