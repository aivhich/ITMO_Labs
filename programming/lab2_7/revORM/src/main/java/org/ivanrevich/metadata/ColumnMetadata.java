package org.ivanrevich.metadata;

import java.lang.reflect.Field;

public class ColumnMetadata {
    private final Field field;
    private final String columnName;
    private final boolean id;
    private final boolean generated;
    public ColumnMetadata(Field field, String columnName, boolean id, boolean generated) {
        this.field = field;
        this.columnName = columnName;
        this.id = id;
        this.generated = generated;
    }

    public Field getField() {
        return field;
    }
    public String getColumnName() {
        return columnName;
    }
    public boolean isId() {
        return id;
    }
    public boolean isGenerated() {
        return generated;
    }
}
