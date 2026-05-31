package org.ivanrevich.mapper;

import org.ivanrevich.metadata.ColumnMetadata;
import org.ivanrevich.metadata.EntityMetadata;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Маппер строк ResultSet → объект сущности через рефлексию.
 * Поддерживает @Embedded поля через ColumnMetadata.setValue().
 */
public class ReflectionMapper {

    public <T> T mapRow(ResultSet rs, Class<T> clazz, EntityMetadata metadata) throws SQLException {
        T instance = newInstance(clazz);

        ColumnMetadata idCol = metadata.getIdColumn();
        setColumn(instance, idCol, rs.getObject(idCol.getColumnName()));

        for (ColumnMetadata col : metadata.getColumns()) {
            if (col.isId()) continue;
            setColumn(instance, col, rs.getObject(col.getColumnName()));
        }

        return instance;
    }

    public <T> List<T> mapAll(ResultSet rs, Class<T> clazz, EntityMetadata metadata) throws SQLException {
        List<T> result = new ArrayList<>();
        while (rs.next()) {
            result.add(mapRow(rs, clazz, metadata));
        }
        return result;
    }

    private void setColumn(Object instance, ColumnMetadata col, Object value) {
        try {
            Object casted = castIfNeeded(col.getField().getType(), value);
            col.setValue(instance, casted);
        } catch (Exception e) {
            throw new OrmMappingException("Cannot set column " + col.getColumnName(), e);
        }
    }

    private <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> ctor = clazz.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor.newInstance();
        } catch (Exception e) {
            throw new OrmMappingException("Cannot instantiate " + clazz.getName(), e);
        }
    }

    private Object castIfNeeded(Class<?> targetType, Object value) {
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;

        if ((targetType == int.class || targetType == Integer.class) && value instanceof Number)
            return ((Number) value).intValue();
        if ((targetType == long.class || targetType == Long.class) && value instanceof Number)
            return ((Number) value).longValue();
        if ((targetType == double.class || targetType == Double.class) && value instanceof Number)
            return ((Number) value).doubleValue();
        if ((targetType == float.class || targetType == Float.class) && value instanceof Number)
            return ((Number) value).floatValue();
        if ((targetType == boolean.class || targetType == Boolean.class) && value instanceof Number)
            return ((Number) value).intValue() != 0;
        if (targetType.isEnum())
            return Enum.valueOf((Class<Enum>) targetType, value.toString());

        return value;
    }

    public static class OrmMappingException extends RuntimeException {
        public OrmMappingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}