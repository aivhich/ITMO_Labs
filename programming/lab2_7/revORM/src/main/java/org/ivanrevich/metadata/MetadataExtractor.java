package org.ivanrevich.metadata;

import org.ivanrevich.annotations.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetadataExtractor {

    public EntityMetadata extract(Class<?> type){
        if(!type.isAnnotationPresent(Entity.class)){
            throw new IllegalArgumentException(type.getName()+" is not annotated with @Entity");
        }
        Table table = type.getAnnotation(Table.class);
        if(table == null){
            throw new IllegalArgumentException(type.getName()+" is not annotated with @Table");
        }

        List<ColumnMetadata> columns = new ArrayList<>();

        ColumnMetadata idColumn = null;

        for(Field field : type.getDeclaredFields()){
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                continue;
            }

            field.setAccessible(true);

            boolean id = field.isAnnotationPresent(Id.class);
            boolean generated = field.isAnnotationPresent(GeneratedValue.class);

            ColumnMetadata metadata = new ColumnMetadata(
                    field,
                    column.name(),
                    id,
                    generated
            );

            columns.add(metadata);

            if(id) {
                idColumn = metadata;
            }
        }
        if(idColumn == null){
            throw new IllegalStateException("Entity must contain @Id field");
        }

        return new EntityMetadata(
                table.name(),
                idColumn,
                columns
        );
    }
}
