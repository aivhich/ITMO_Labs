package mapper;

import org.ivanrevich.annotations.*;
import org.ivanrevich.mapper.ReflectionMapper;
import org.ivanrevich.metadata.EntityMetadata;
import org.ivanrevich.metadata.MetadataExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ReflectionMapper Tests")
class ReflectionMapperTest {

    @Entity
    @Table(name = "widgets")
    public static class Widget {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private int id;

        @Column(name = "label")
        private String label;

        @Column(name = "price")
        private double price;

        public int getId() { return id; }
        public String getLabel() { return label; }
        public double getPrice() { return price; }
    }

    /**
     * Builds a dynamic proxy implementing ResultSet, backed by a row map.
     * Only getObject(String) and next() are actually exercised by ReflectionMapper.
     */
    private ResultSet fakeResultSet(Map<String, Object> row) {
        InvocationHandler handler = (proxy, method, args) -> {
            switch (method.getName()) {
                case "getObject":
                    return row.get((String) args[0]);
                case "next":
                    return false; // single-row mapRow() test doesn't iterate
                default:
                    return defaultReturnValue(method.getReturnType());
            }
        };
        return (ResultSet) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class<?>[]{ResultSet.class},
                handler);
    }

    private Object defaultReturnValue(Class<?> type) {
        if (type == boolean.class) return false;
        if (type == int.class) return 0;
        return null;
    }

    @Test
    @DisplayName("mapRow() correctly populates fields from a ResultSet row")
    void mapRowPopulatesFields() throws Exception {
        Map<String, Object> row = new HashMap<>();
        row.put("id", 7);
        row.put("label", "Gadget");
        row.put("price", 19.99);

        EntityMetadata metadata = new MetadataExtractor().extract(Widget.class);
        ReflectionMapper mapper = new ReflectionMapper();

        Widget w = mapper.mapRow(fakeResultSet(row), Widget.class, metadata);

        assertEquals(7, w.getId());
        assertEquals("Gadget", w.getLabel());
        assertEquals(19.99, w.getPrice());
    }

    @Test
    @DisplayName("mapRow() casts numeric types correctly (Integer -> int field)")
    void mapRowCastsNumericTypes() throws Exception {
        Map<String, Object> row = new HashMap<>();
        row.put("id", 5L); // comes back as Long from some DB drivers
        row.put("label", "X");
        row.put("price", 1); // comes back as Integer instead of double

        EntityMetadata metadata = new MetadataExtractor().extract(Widget.class);
        ReflectionMapper mapper = new ReflectionMapper();

        Widget w = mapper.mapRow(fakeResultSet(row), Widget.class, metadata);

        assertEquals(5, w.getId());
        assertEquals(1.0, w.getPrice());
    }

    @Test
    @DisplayName("mapRow() throws OrmMappingException wrapping reflective errors")
    void mapRowWrapsErrors() {
        // Entity without a no-arg constructor accessible via reflection cleanly will still work
        // here since Widget has an implicit no-arg constructor; instead force a type mismatch
        // that the caster cannot resolve by using an incompatible enum value.
        Map<String, Object> row = new HashMap<>();
        row.put("id", "not-a-number"); // String cannot be cast to int by castIfNeeded
        row.put("label", "X");
        row.put("price", 1.0);

        EntityMetadata metadata = new MetadataExtractor().extract(Widget.class);
        ReflectionMapper mapper = new ReflectionMapper();

        assertThrows(ReflectionMapper.OrmMappingException.class,
                () -> mapper.mapRow(fakeResultSet(row), Widget.class, metadata));
    }
}
