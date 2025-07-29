package learn.petnote.domain;

import learn.petnote.data.NoteRepository;
import learn.petnote.data.PetRepository;
import learn.petnote.models.Note;
import learn.petnote.models.Pet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

        private final NoteRepository repository;
        private final PetRepository petRepository;

        public NoteService(NoteRepository repository, PetRepository petRepository) {
            this.repository = repository;
            this.petRepository = petRepository;
        }

        // ---------- Create ----------
        public Result<Note> create(Note note) {
            Result<Note> result = validate(note, true);
            if (!result.isSuccess()) return result;

            // Ensure pet exists and belongs to user
            Pet pet = petRepository.findById(note.getPetId());
            if (pet == null) {
                result.addErrorMessage("Pet not found.", ResultType.NOT_FOUND);
                return result;
            }
            if (pet.getUserId() != note.getUserId()) {
                result.addErrorMessage("Unauthorized: pet does not belong to user.", ResultType.INVALID);
                return result;
            }

            note.setCreatedAt(LocalDateTime.now());
            Note created = repository.create(note);
            if (created == null) {
                result.addErrorMessage("Failed to create note.", ResultType.INVALID);
            } else {
                result.setpayload(created);
            }
            return result;
        }

        // ---------- Read ----------
        public List<Note> findByPetId(int petId) {
            return repository.findByPetId(petId);
        }

        public Note findById(int id) {
            return repository.findById(id);
        }

        // ---------- Update ----------
        public Result<Note> update(Note note) {
            Result<Note> result = validate(note, false);
            if (!result.isSuccess()) return result;

            Note existing = repository.findById(note.getId());
            if (existing == null) {
                result.addErrorMessage("Note not found.", ResultType.NOT_FOUND);
                return result;
            }

            Pet pet = petRepository.findById(existing.getPetId());
            if (pet == null || pet.getUserId() != note.getUserId()) {
                result.addErrorMessage("Unauthorized: pet does not belong to user.", ResultType.INVALID);
                return result;
            }

            note.setEditedAt(LocalDateTime.now());
            note.setCreatedAt(existing.getCreatedAt()); // keep original creation time

            if (!repository.update(note)) {
                result.addErrorMessage("Update failed.", ResultType.INVALID);
            } else {
                result.setpayload(note);
            }
            return result;
        }

        // ---------- Delete ----------
        public Result<Void> deleteById(int id, int userId) {
            Result<Void> result = new Result<>();

            Note existing = repository.findById(id);
            if (existing == null) {
                result.addErrorMessage("Note not found.", ResultType.NOT_FOUND);
                return result;
            }

            Pet pet = petRepository.findById(existing.getPetId());
            if (pet == null || pet.getUserId() != userId) {
                result.addErrorMessage("Unauthorized: pet does not belong to user.", ResultType.INVALID);
                return result;
            }

            if (!repository.deleteById(id)) {
                result.addErrorMessage("Delete failed.", ResultType.INVALID);
            }

            return result;
        }

        // ---------- Validation ----------
        private Result<Note> validate(Note note, boolean isCreate) {
            Result<Note> result = new Result<>();

            if (note == null) {
                result.addErrorMessage("Note cannot be null.", ResultType.INVALID);
                return result;
            }
            if (!isCreate && note.getId() <= 0) {
                result.addErrorMessage("Note id is required for update.", ResultType.INVALID);
            }
            if (note.getPetId() <= 0) {
                result.addErrorMessage("petId is required.", ResultType.INVALID);
            }
            if (note.getUserId() <= 0) {
                result.addErrorMessage("userId is required.", ResultType.INVALID);
            }
            if (note.getDescription() == null || note.getDescription().isBlank()) {
                result.addErrorMessage("description is required.", ResultType.INVALID);
            }
            return result;
        }
    }
