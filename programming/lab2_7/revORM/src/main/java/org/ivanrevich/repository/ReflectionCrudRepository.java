package org.ivanrevich.repository;

import org.ivanrevich.mapper.ReflectionMapper;
import org.ivanrevich.metadata.ColumnMetadata;
import org.ivanrevich.metadata.EntityMetadata;
import org.ivanrevich.metadata.MetadataExtractor;
import org.ivanrevich.sql.SqlGenerator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class ReflectionCrudRepository<T, ID> implements CrudRepository<T, ID> {

    private final DataSource dataSource;
    private final Class<T> clazz;
    private final EntityMetadata metadata;
    private final SqlGenerator sqlGenerator = new SqlGenerator();
    private final ReflectionMapper mapper = new ReflectionMapper();

    public ReflectionCrudRepository(DataSource dataSource, Class<T> clazz) {
        this.dataSource = dataSource;
        this.clazz = clazz;
        this.metadata = new MetadataExtractor().extract(clazz);
        initTable();
    }

    @Override
    public Optional<T> findByField(String columnName, Object value) {
        String sql = String.format("SELECT * FROM %s WHERE %s = ?", metadata.getTableName(), columnName);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            bindValue(ps, 1, value);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapper.mapRow(rs, clazz, metadata));
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new OrmException("findByField() failed", e);
        }
    }

    @Override
    public void initTable() {
        String sql = sqlGenerator.buildCreateTable(metadata);
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new OrmException("initTable() failed for " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public T save(T entity)  {
        String sql = sqlGenerator.buildInsert(metadata);
        try(
        Connection con = dataSource.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);

            bindInsertParams(ps, entity);
            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    ColumnMetadata idCol = metadata.getIdColumn();
                    idCol.setValue(entity, rs.getObject(1));
                }
            }
            return entity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(T entity) {
        String sql = sqlGenerator.buildUpdate(metadata);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            int paramIdx = bindUpdateParams(ps, entity);
            ColumnMetadata idCol = metadata.getIdColumn();
            ps.setObject(paramIdx, idCol.getValue(entity));
            ps.executeUpdate();

        } catch (Exception e) {
            throw new OrmException("update() failed for " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public Optional<T> findById(ID id) {
        String sql = sqlGenerator.buildSelectById(metadata);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapper.mapRow(rs, clazz, metadata));
                return Optional.empty();
            }
        } catch (Exception e) {
            throw new OrmException("findById() failed for " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public List<T> findAll() {
        String sql = sqlGenerator.buildSelectAll(metadata);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return mapper.mapAll(rs, clazz, metadata);
        } catch (Exception e) {
            throw new OrmException("findAll() failed for " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public void deleteById(ID id) {
        String sql = sqlGenerator.buildDelete(metadata);
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setObject(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new OrmException("deleteById() failed for " + clazz.getSimpleName(), e);
        }
    }

    @Override
    public boolean existsById(ID id) {
        return findById(id).isPresent();
    }


    private void bindInsertParams(PreparedStatement ps, T entity) throws Exception {
        int i = 1;
        for (ColumnMetadata col : metadata.getColumns()) {
            if (col.isGenerated()) continue;
            Object value = col.getValue(entity);
            bindValue(ps, i++, value);
        }
    }

    private int bindUpdateParams(PreparedStatement ps, T entity) throws Exception {
        int i = 1;
        for (ColumnMetadata col : metadata.getColumns()) {
            if (col.isId()) continue;
            Object value = col.getValue(entity);
            bindValue(ps, i++, value);
        }
        return i;
    }

    private void bindValue(PreparedStatement ps, int index, Object value) throws SQLException {
        if (value == null) {
            ps.setObject(index, null);
        } else if (value instanceof java.util.Date) {
            java.util.Date date = (java.util.Date) value;
            ps.setTimestamp(index, new java.sql.Timestamp(date.getTime()));
        } else if (value instanceof Enum) {
            ps.setString(index, ((Enum<?>) value).name());
        } else if(value instanceof byte[]) {
            ps.setBytes(index, (byte[]) value);
        }
        else {
            ps.setObject(index, value);
        }
    }

    public static class OrmException extends RuntimeException {
        public OrmException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}