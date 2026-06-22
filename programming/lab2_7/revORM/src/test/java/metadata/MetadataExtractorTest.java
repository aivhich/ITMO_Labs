package metadata;

import org.ivanrevich.annotations.*;
import org.ivanrevich.metadata.EntityMetadata;
import org.ivanrevich.metadata.MetadataExtractor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MetadataExtractor Tests")
class MetadataExtractorTest {

    @Entity
    @Table(name = "simple_entity")
    static class SimpleEntity {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private int id;

        @Column(name = "name")
        private String name;

        @Column(name = "score")
        @Unique
        private double score;
    }

    static class NotAnEntity {
        @Id
        @Column(name = "id")
        private int id;
    }

    @Entity
    static class MissingTableAnnotation {
        @Id
        @Column(name = "id")
        private int id;
    }

    @Entity
    @Table(name = "no_id_entity")
    static class NoIdEntity {
        @Column(name = "value")
        private String value;
    }

    @Entity
    @Table(name = "embedded_entity")
    static class Inner {
        private double x;
        private float y;
    }

    @Entity
    @Table(name = "with_embedded")
    static class WithEmbedded {
        @Id
        @GeneratedValue
        @Column(name = "id")
        private int id;

        @Embedded(overrides = {
                @AttributeOverride(field = "x", column = "coord_x"),
                @AttributeOverride(field = "y", column = "coord_y")
        })
        private Inner inner;
    }

    private final MetadataExtractor extractor = new MetadataExtractor();

    @Test
    @DisplayName("extracts table name correctly")
    void extractsTableName() {
        EntityMetadata meta = extractor.extract(SimpleEntity.class);
        assertEquals("simple_entity", meta.getTableName());
    }

    @Test
    @DisplayName("identifies the @Id column")
    void identifiesIdColumn() {
        EntityMetadata meta = extractor.extract(SimpleEntity.class);
        assertNotNull(meta.getIdColumn());
        assertEquals("id", meta.getIdColumn().getColumnName());
        assertTrue(meta.getIdColumn().isId());
        assertTrue(meta.getIdColumn().isGenerated());
    }

    @Test
    @DisplayName("extracts all @Column fields")
    void extractsAllColumns() {
        EntityMetadata meta = extractor.extract(SimpleEntity.class);
        // id, name, score
        assertEquals(3, meta.getColumns().size());
    }

    @Test
    @DisplayName("marks @Unique field correctly")
    void marksUniqueField() {
        EntityMetadata meta = extractor.extract(SimpleEntity.class);
        boolean scoreUnique = meta.getColumns().stream()
                .filter(c -> c.getColumnName().equals("score"))
                .findFirst().orElseThrow().isUnique();
        assertTrue(scoreUnique);
    }

    @Test
    @DisplayName("class without @Entity throws IllegalArgumentException")
    void noEntityAnnotationThrows() {
        assertThrows(IllegalArgumentException.class, () -> extractor.extract(NotAnEntity.class));
    }

    @Test
    @DisplayName("class with @Entity but no @Table throws IllegalArgumentException")
    void noTableAnnotationThrows() {
        assertThrows(IllegalArgumentException.class, () -> extractor.extract(MissingTableAnnotation.class));
    }

    @Test
    @DisplayName("entity without @Id field throws IllegalStateException")
    void noIdFieldThrows() {
        assertThrows(IllegalStateException.class, () -> extractor.extract(NoIdEntity.class));
    }

    @Test
    @DisplayName("@Embedded field columns are extracted with overrides")
    void embeddedFieldExtracted() {
        EntityMetadata meta = extractor.extract(WithEmbedded.class);
        // id + embedded x,y = 3 columns total
        assertEquals(3, meta.getColumns().size());
        assertTrue(meta.getColumns().stream().anyMatch(c -> c.getColumnName().equals("coord_x")));
        assertTrue(meta.getColumns().stream().anyMatch(c -> c.getColumnName().equals("coord_y")));
    }

    @Test
    @DisplayName("embedded columns report isEmbedded() = true")
    void embeddedColumnsMarkedEmbedded() {
        EntityMetadata meta = extractor.extract(WithEmbedded.class);
        boolean anyEmbedded = meta.getColumns().stream()
                .filter(c -> c.getColumnName().equals("coord_x"))
                .findFirst().orElseThrow().isEmbedded();
        assertTrue(anyEmbedded);
    }
}
