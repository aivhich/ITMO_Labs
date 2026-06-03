package org.ivanrevich.metadata;

import java.util.List;

public class EntityMetadata {
    private final String tableName;
    private final ColumnMetadata idColumn;
    private final List<ColumnMetadata> columns;

    public EntityMetadata(String tableName, ColumnMetadata idColumn, List<ColumnMetadata> columns) {
        this.tableName = tableName;
        this.idColumn = idColumn;
        this.columns = columns;
    }
    public String getTableName() {
        return tableName;
    }
    public ColumnMetadata getIdColumn(){
        return idColumn;
    }
    public List<ColumnMetadata> getColumns() {return columns;}
}
