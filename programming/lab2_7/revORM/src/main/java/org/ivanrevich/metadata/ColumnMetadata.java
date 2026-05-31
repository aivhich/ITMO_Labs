package org.ivanrevich.metadata;

import java.lang.reflect.Field;

public class ColumnMetadata {
    private final Field field;
    private final String columnName;
    private final boolean id;
    private final boolean generated;

    private final Field parentField;

    public ColumnMetadata(Field field, String columnName, boolean id, boolean generated) {
        this.field = field;
        this.columnName = columnName;
        this.id = id;
        this.generated = generated;
        this.parentField = null;
    }

    public ColumnMetadata(Field parentField, Field field, String columnName, boolean id, boolean generated) {
        this.parentField = parentField;
        this.field = field;
        this.columnName = columnName;
        this.id = id;
        this.generated = generated;
    }

    public boolean isEmbedded() {
        return parentField != null;
    }
    public Object getValue(Object entity) throws IllegalAccessException {
        if (parentField != null) {
            parentField.setAccessible(true);
            Object embedded = parentField.get(entity);
            if (embedded == null) return null;
            field.setAccessible(true);
            return field.get(embedded);
        }
        field.setAccessible(true);
        return field.get(entity);
    }

    public void setValue(Object entity, Object value) throws Exception {
        if (parentField != null) {
            parentField.setAccessible(true);
            Object embedded = parentField.get(entity);
            if (embedded == null) {
                embedded = parentField.getType().getDeclaredConstructor().newInstance();
                parentField.set(entity, embedded);
            }
            field.setAccessible(true);
            field.set(embedded, value);
        } else {
            field.setAccessible(true);
            field.set(entity, value);
        }
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
