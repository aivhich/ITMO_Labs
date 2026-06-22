package commands;

import org.ivanrevich.managers.IOManager;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Records every write() call; read-family methods are unused in
 * server-side Request-driven command tests but implemented to satisfy
 * the interface contract.
 */
public class RecordingIOManager implements IOManager {
    public final List<String> lines = new ArrayList<>();

    @Override
    public String getFile() { return "TEST"; }

    @Override
    public String read() { return ""; }

    @Override
    public void write(String text) { lines.add(text); }

    @Override
    public String askString(String text) { write(text); return ""; }

    @Override
    public Long askLong(String text) { write(text); return 0L; }

    @Override
    public Double askDouble(String text) { write(text); return 0.0; }

    @Override
    public Float askFloat(String text) { write(text); return 0f; }

    @Override
    public Integer askInt(String text) { write(text); return 0; }

    @Override
    public <T> T askValue(T initValue, Supplier<T> input, Predicate<T> validator) {
        return initValue;
    }
}
