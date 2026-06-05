package org.ivanrevich.sql;

import org.ivanrevich.metadata.ColumnMetadata;
import org.ivanrevich.metadata.EntityMetadata;

import java.util.Date;

public class SqlGenerator {
    private String resolveType(ColumnMetadata col) {
        Class<?> type = col.getField().getType();
        if (type == String.class) return "VARCHAR(255) NOT NULL";
        if (type == byte[].class) return "bytea NOT NULL";
        if (type == int.class || type == Integer.class) return "INTEGER NOT NULL";
        if (type == long.class || type == Long.class) return "BIGINT NOT NULL";
        if (type == float.class || type == Float.class) return "REAL NOT NULL";
        if (type == double.class || type == Double.class) return "DOUBLE PRECISION NOT NULL";
        if (type == boolean.class || type == Boolean.class) return "BOOLEAN NOT NULL";
        if (type.isEnum()) return "VARCHAR(50) NOT NULL";
        if (type == java.util.Date.class) return "TIMESTAMP NOT NULL";
        return "TEXT";
    }

    public String buildCreateTable(EntityMetadata metadata) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ").append(metadata.getTableName()).append(" (\n");

        ColumnMetadata idCol = metadata.getIdColumn();
        sb.append("  ").append(idCol.getColumnName()).append(" SERIAL PRIMARY KEY,\n");

        for (ColumnMetadata col : metadata.getColumns()) {
            if (col.isId()) continue;
            sb.append("  ").append(col.getColumnName()).append(" ").append(resolveType(col)).append(col.isUnique() ? " UNIQUE" : "").append(",\n");
        }

        sb.setLength(sb.length() - 2);
        sb.append("\n);");
        return sb.toString();
    }

    public String buildInsert(EntityMetadata metadata){
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for(var col : metadata.getColumns()){
            if(col.isGenerated()){
                continue;
            }
            columns.append(col.getColumnName())
                    .append(",");
            values.append("?,");
        }
        columns.setLength(columns.length()-1);
        values.setLength(values.length()-1);
        return String.format("" +
                "INSERT INTO %s (%s) VALUES (%s) RETURNING %s",
                metadata.getTableName(),
                columns,
                values,
                metadata.getIdColumn().getColumnName());
    }

    public String buildSelectById(EntityMetadata metadata){
        return String.format(
                """
                SELECT *
                FROM %s
                WHERE %s = ?
                """,
                metadata.getTableName(),
                metadata.getIdColumn()
                        .getColumnName()
        );
    }

    public String buildSelectAll(EntityMetadata metadata){
        return String.format(
                """
                SELECT *
                FROM %s
                """,
                metadata.getTableName()
        );
    }
    public String buildUpdate(EntityMetadata metadata){
        StringBuilder setClause = new StringBuilder();
        for (var column : metadata.getColumns()) {
            if (column.isId()) continue;
            setClause.append(column.getColumnName()).append("=?,");
        }
        setClause.setLength(setClause.length() - 1);
        return String.format(
                """
                UPDATE %s
                SET %s
                WHERE %s = ?
                """,
                metadata.getTableName(),
                setClause, metadata.getIdColumn().getColumnName()
        );
    }
    public String buildDelete(EntityMetadata metadata){
        return String.format(
                """
                DELETE
                FROM %s
                WHERE %s = ?
                """,
                metadata.getTableName(),
                metadata.getIdColumn().getColumnName()
        );
    }

}
