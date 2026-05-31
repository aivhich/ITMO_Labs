package org.ivanrevich.sql;

import org.ivanrevich.metadata.EntityMetadata;

public class SqlGenerator {
    public String buildInsert(EntityMetadata metadata){
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        for(var col : metadata.getColumns()){

            if(col.isGenerated()){
                continue;
            }
            columns
                    .append(col.getColumnName())
                    .append(",");
            values
                    .append("?,");
        }
        columns.setLength(columns.length()-1);
        values.setLength(values.length()-1);
        return String.format("" +
                "INSERT INRO %s (%s) VALUES (%s) RETURNING %s",
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
        StringBuilder setClause =
                new StringBuilder();

        for (var column : metadata.getColumns()) {

            if (column.isId()) {
                continue;
            }

            setClause
                    .append(column.getColumnName())
                    .append("=?,");
        }

        setClause.setLength(
                setClause.length() - 1
        );

        return String.format(
                """
                UPDATE %s
                SET %s
                WHERE %s = ?
                """,
                metadata.getTableName(),
                setClause,
                metadata.getIdColumn()
                        .getColumnName()
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
                metadata.getIdColumn()
                        .getColumnName()
        );
    }

}
