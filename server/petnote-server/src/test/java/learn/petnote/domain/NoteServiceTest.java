package learn.petnote.domain;

import learn.petnote.data.NoteRepository;
import learn.petnote.data.PetRepository;
import learn.petnote.models.Note;
import learn.petnote.models.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class NoteServiceTest {

    @Autowired
    NoteService service;
    @MockBean
    private NoteRepository noteRepository;

    @MockBean
    private PetRepository petRepository;

    private Pet pet;

    @BeforeEach
    void setup() {
        pet = new Pet();
        pet.setId(10);
        pet.setUserId(1);
        pet.setPetName("Fido");
    }

    @Test
    void create_ok() {
        Note toCreate = new Note();
        toCreate.setPetId(10);
        toCreate.setUserId(1);
        toCreate.setDescription("Hello");

        when(petRepository.findById(10)).thenReturn(pet);

        Note saved = new Note();
        saved.setId(100);
        saved.setPetId(10);
        saved.setUserId(1);
        saved.setDescription("Hello");
        saved.setCreatedAt(LocalDateTime.now());

        when(noteRepository.create(any(Note.class))).thenReturn(saved);

        Result<Note> result = service.create(toCreate);

        assertTrue(result.isSuccess());
        assertEquals(100, result.getpayload().getId());
        verify(noteRepository).create(any(Note.class));
    }

    @Test
    void create_petNotFound() {
        Note toCreate = new Note();
        toCreate.setPetId(999);
        toCreate.setUserId(1);
        toCreate.setDescription("Hello");

        when(petRepository.findById(999)).thenReturn(null);

        Result<Note> result = service.create(toCreate);

        assertFalse(result.isSuccess());
        assertEquals(ResultType.NOT_FOUND, result.getResultType());
        verify(noteRepository, never()).create(any());
    }

    @Test
    void create_unauthorizedUser() {
        Note toCreate = new Note();
        toCreate.setPetId(10);
        toCreate.setUserId(2); // wrong owner
        toCreate.setDescription("Hello");

        pet.setUserId(1);
        when(petRepository.findById(10)).thenReturn(pet);

        Result<Note> result = service.create(toCreate);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResultType()).isEqualTo(ResultType.INVALID);
        verify(noteRepository, never()).create(any());
    }

    @Test
    void update_ok_setsEditedAt_andKeepsCreatedAt() {
        Note existing = new Note();
        existing.setId(50);
        existing.setPetId(10);
        existing.setUserId(1);
        existing.setDescription("Old");
        existing.setCreatedAt(LocalDateTime.now().minusDays(1));

        when(noteRepository.findById(50)).thenReturn(existing);
        when(petRepository.findById(10)).thenReturn(pet);
        when(noteRepository.update(any(Note.class))).thenReturn(true);

        Note updateReq = new Note();
        updateReq.setId(50);
        updateReq.setPetId(10);
        updateReq.setUserId(1);
        updateReq.setDescription("New Description");

        Result<Note> result = service.update(updateReq);

        assertThat(result.isSuccess()).isTrue();
        Note updated = result.getpayload();
        assertThat(updated.getDescription()).isEqualTo("New Description");
        assertThat(updated.getCreatedAt()).isEqualTo(existing.getCreatedAt());
        assertThat(updated.getEditedAt()).isNotNull();

        verify(noteRepository).update(any(Note.class));
    }

    @Test
    void update_notFound() {
        when(noteRepository.findById(999)).thenReturn(null);

        Note req = new Note();
        req.setId(999);
        req.setPetId(10);
        req.setUserId(1);
        req.setDescription("X");

        Result<Note> result = service.update(req);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResultType()).isEqualTo(ResultType.NOT_FOUND);
        verify(noteRepository, never()).update(any());
    }

    @Test
    void delete_ok() {
        Note existing = new Note();
        existing.setId(77);
        existing.setPetId(10);
        existing.setUserId(1);

        when(noteRepository.findById(77)).thenReturn(existing);
        when(petRepository.findById(10)).thenReturn(pet);
        when(noteRepository.deleteById(77)).thenReturn(true);

        Result<Void> result = service.deleteById(77, 1);

        assertThat(result.isSuccess()).isTrue();
        verify(noteRepository).deleteById(77);
    }

    @Test
    void delete_unauthorized() {
        Note existing = new Note();
        existing.setId(77);
        existing.setPetId(10);
        existing.setUserId(1);

        pet.setUserId(999); // different owner
        when(noteRepository.findById(77)).thenReturn(existing);
        when(petRepository.findById(10)).thenReturn(pet);

        Result<Void> result = service.deleteById(77, 1);

        assertThat(result.isSuccess()).isFalse();
        assertThat(result.getResultType()).isEqualTo(ResultType.INVALID);
        verify(noteRepository, never()).deleteById(anyInt());
    }

    @Test
    void validate_missingFields_create() {
        // petId / userId / content missing
        Result<Note> result = service.create(new Note());
        assertFalse(result.isSuccess());
        assertTrue(result.getErrorMessages().contains("petId is required."));
        assertTrue(result.getErrorMessages().contains("userId is required."));
        assertTrue(result.getErrorMessages().contains("description is required."));
    }

    @Test
    void findByPetId_delegatesToRepo() {
        when(noteRepository.findByPetId(10)).thenReturn(Collections.emptyList());
        List<Note> notes = service.findByPetId(10);
        assertTrue((notes).isEmpty());
        verify(noteRepository).findByPetId(10);
    }
}
