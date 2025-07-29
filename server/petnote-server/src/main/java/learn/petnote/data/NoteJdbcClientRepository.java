package learn.petnote.data;

import learn.petnote.models.Note;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class NoteJdbcClientRepository implements NoteRepository {

    private final JdbcClient jdbcClient;

    public NoteJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<Note> findByPetId(int petId) {
        final String sql = """
                SELECT id, petId, userId, description, createdAt, editedAt from
                note where petId = :petId""";

        return jdbcClient.sql(sql)
                .param("petId", petId)
                .query(Note.class)
                .list();
    }

    @Override
    public Note findById(int id) {
        final String sql = """
                select id, petId, userId, description, createdAt, editedAt
                from note where id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Note.class)
                .optional()
                .orElse(null);
    }


    @Override
    public Note create(Note note) {
        final String sql = """
                insert into note (petId, userId, description, createdAt, editedAt)
                values (:petId, :userId, :description, :createdAt, :editedAt)""";

        KeyHolder keyHolder = new GeneratedKeyHolder();

         jdbcClient.sql(sql)
                .param("petId", note.getPetId())
                .param("userId", note.getUserId())
                .param("description", note.getDescription())
                .param("createdAt", note.getCreatedAt())
                 .param("editedAt", LocalDateTime.now())
                .update(keyHolder, "id");

        note.setId(keyHolder.getKey().intValue());

        int newId = keyHolder.getKey().intValue();

        return findById(newId);
    }

    @Override
    public boolean update(Note note) {
        final String sql = """
                update note 
                set description = :description, editedAt = :editedAt
                where id = :id
                """;

        return jdbcClient.sql(sql)
                .param("description", note.getDescription())
                .param("editedAt", LocalDateTime.now())
                .param("id", note.getId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = """
                delete from note where id = :id
                """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .update() > 0;
    }
}
