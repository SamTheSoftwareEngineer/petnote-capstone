package learn.petnote.data;

import learn.petnote.models.Note;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NoteJdbcClientRepositoryTest {

    @Autowired
    private NoteJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setUp() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void findByPetId() {
        List<Note> notes = repository.findByPetId(1);
        assertThat(notes).isNotNull();
        assertThat(notes).isNotEmpty();
        assertThat(notes.get(0).getPetId()).isEqualTo(1);
    }

    @Test
    void findById() {
        Note note = repository.findById(1);
        assertThat(note).isNotNull();
        assertThat(note.getId()).isEqualTo(1);
        assertThat(note.getDescription()).isNotBlank();
    }

    @Test
    void create() {
        Note newNote = new Note();
        newNote.setPetId(1);
        newNote.setUserId(1);
        newNote.setDescription("Vet appointment scheduled.");
        newNote.setCreatedAt(LocalDateTime.now());
        newNote.setEditedAt(LocalDateTime.now());

        Note created = repository.create(newNote);
        assertThat(created).isNotNull();
        assertThat(created.getId()).isGreaterThan(0);
        assertThat(created.getDescription()).isEqualTo("Vet appointment scheduled.");

        Note fetched = repository.findById(created.getId());
        assertThat(fetched).isNotNull();
        assertThat(fetched.getDescription()).isEqualTo("Vet appointment scheduled.");
    }

    @Test
    void update() {
        Note note = repository.findById(1);
        note.setDescription("Updated content for testing.");
        note.setEditedAt(LocalDateTime.now());

        boolean updated = repository.update(note);
        assertThat(updated).isTrue();

        Note fetched = repository.findById(1);
        assertThat(fetched.getDescription()).isEqualTo("Updated content for testing.");
        assertThat(fetched.getEditedAt()).isNotNull();
    }

    @Test
    void deleteById() {
        Note note = repository.create(new Note(0, LocalDateTime.now(), LocalDateTime.now(),  "Temp note for deletion", 1, 1));
        int id = note.getId();

        boolean deleted = repository.deleteById(id);
        assertThat(deleted).isTrue();

        Note fetched = repository.findById(id);
        assertThat(fetched).isNull();
    }
}
