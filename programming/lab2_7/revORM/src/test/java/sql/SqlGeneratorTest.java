package sql;

import org.ivanrevich.annotations.*;
import org.ivanrevich.metadata.EntityMetadata;
import org.ivanrevich.metadata.MetadataExtractor;
import org.ivanrevich.sql.SqlGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SqlGenerator Tests")
class SqlGeneratorTest {

    @Entity
    @Table(name = "things")
    static class Thing {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private int id;

        @Column(name = "title")
        private String title;

        @Column(name = "amount")
        private double amount;
    }

    private final SqlGenerator generator = new SqlGenerator();
    private EntityMetadata metadata;

    @BeforeEach
    void setUp() {
        metadata = new MetadataExtractor().extract(Thing.class);
    }

    @Test
    @DisplayName("buildCreateTable() contains table name and all columns")
    void createTableContainsColumns() {
        String sql = generator.buildCreateTable(metadata);
        assertTrue(sql.contains("CREATE TABLE IF NOT EXISTS things"));
        assertTrue(sql.contains("id SERIAL PRIMARY KEY"));
        assertTrue(sql.contains("title"));
        assertTrue(sql.contains("amount"));
    }

    @Test
    @DisplayName("buildInsert() excludes generated id column")
    void insertExcludesGeneratedId() {
        String sql = generator.buildInsert(metadata);
        assertTrue(sql.startsWith("INSERT INTO things"));
        assertFalse(sql.contains("(id,"));
        assertTrue(sql.contains("title"));
        assertTrue(sql.contains("amount"));
        assertTrue(sql.contains("RETURNING id"));
    }

    @Test
    @DisplayName("buildInsert() has matching placeholder count")
    void insertHasMatchingPlaceholders() {
        String sql = generator.buildInsert(metadata);
        long questionMarks = sql.chars().filter(c -> c == '?').count();
        assertEquals(2, questionMarks);
    }

    @Test
    @DisplayName("buildSelectById() filters by id column")
    void selectByIdFiltersById() {
        String sql = generator.buildSelectById(metadata);
        assertTrue(sql.contains("SELECT *"));
        assertTrue(sql.contains("FROM things"));
        assertTrue(sql.contains("WHERE id = ?"));
    }

    @Test
    @DisplayName("buildSelectAll() selects from correct table")
    void selectAllFromTable() {
        String sql = generator.buildSelectAll(metadata);
        assertTrue(sql.contains("SELECT *"));
        assertTrue(sql.contains("FROM things"));
    }

    @Test
    @DisplayName("buildUpdate() sets all non-id columns and filters by id")
    void updateSetsNonIdColumns() {
        String sql = generator.buildUpdate(metadata);
        assertTrue(sql.contains("UPDATE things"));
        assertTrue(sql.contains("title=?"));
        assertTrue(sql.contains("amount=?"));
        assertTrue(sql.contains("WHERE id = ?"));
        assertFalse(sql.contains("id=?"));
    }

    @Test
    @DisplayName("buildDelete() deletes by id column")
    void deleteByIdColumn() {
        String sql = generator.buildDelete(metadata);
        assertTrue(sql.contains("DELETE"));
        assertTrue(sql.contains("FROM things"));
        assertTrue(sql.contains("WHERE id = ?"));
    }
}
