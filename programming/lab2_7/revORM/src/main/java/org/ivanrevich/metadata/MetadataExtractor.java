package org.ivanrevich.metadata;

import org.ivanrevich.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetadataExtractor {

    public EntityMetadata extract(Class<?> type) {
        if (!type.isAnnotationPresent(Entity.class)) {
            throw new IllegalArgumentException(type.getName() + " is not annotated with @Entity");
        }
        Table table = type.getAnnotation(Table.class);
        if (table == null) {
            throw new IllegalArgumentException(type.getName() + " is not annotated with @Table");
        }

        List<ColumnMetadata> columns = new ArrayList<>();
        ColumnMetadata idColumn = null;

        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(Embedded.class)) {
                Embedded embedded = field.getAnnotation(Embedded.class);

                Map<String, String> overrideMap = new HashMap<>();
                for (AttributeOverride override : embedded.overrides()) {
                    overrideMap.put(override.field(), override.column());
                }

                for (Field innerField : field.getType().getDeclaredFields()) {
                    Column innerColumn = innerField.getAnnotation(Column.class);
                    String columnName;
                    if (overrideMap.containsKey(innerField.getName())) {
                        columnName = overrideMap.get(innerField.getName());
                    } else if (innerColumn != null) {
                        columnName = innerColumn.name();
                    } else {
                        continue;
                    }

                    innerField.setAccessible(true);
                    boolean isId = innerField.isAnnotationPresent(Id.class);
                    boolean isGenerated = innerField.isAnnotationPresent(GeneratedValue.class);

                    ColumnMetadata meta = new ColumnMetadata(field, innerField, columnName, isId, isGenerated);
                    columns.add(meta);

                    if (isId) {
                        if (idColumn != null) throw new IllegalStateException("Multiple @Id found in " + type.getName());
                        idColumn = meta;
                    }
                }
                continue;
            }

            Column column = field.getAnnotation(Column.class);
            if (column == null) continue;

            boolean isId = field.isAnnotationPresent(Id.class);
            boolean isGenerated = field.isAnnotationPresent(GeneratedValue.class);

            ColumnMetadata meta = new ColumnMetadata(field, column.name(), isId, isGenerated);
            columns.add(meta);

            if (isId) {
                if (idColumn != null) throw new IllegalStateException("Multiple @Id found in " + type.getName());
                idColumn = meta;
            }
        }

        if (idColumn == null) {
            throw new IllegalStateException("Entity " + type.getName() + " must contain @Id field");
        }

        return new EntityMetadata(table.name(), idColumn, columns);
    }
}