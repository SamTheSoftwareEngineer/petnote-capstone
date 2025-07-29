package learn.petnote.data;

import learn.petnote.models.Note;

import java.util.List;

public interface NoteRepository {
    List<Note> findByPetId(int petId);
    Note findById(int id);
    Note create(Note note);
    boolean update(Note note);
    boolean deleteById(int id);
}
