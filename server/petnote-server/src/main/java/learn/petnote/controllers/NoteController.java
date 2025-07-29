package learn.petnote.controllers;

import learn.petnote.domain.NoteService;
import learn.petnote.domain.Result;
import learn.petnote.models.Note;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:5173")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    // -------- CREATE --------
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Note note) {
        Result<Note> result = service.create(note);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrorMessages());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getpayload());
    }

    // -------- READ --------
    @GetMapping("/pet/{petId}")
    public List<Note> findByPetId(@PathVariable int petId) {
        return service.findByPetId(petId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> findById(@PathVariable int id) {
        Note note = service.findById(id);
        return (note == null)
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(note);
    }

    // -------- UPDATE --------
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Note note) {
        if (id != note.getId()) {
            return ResponseEntity.badRequest().body("ID mismatch.");
        }
        Result<Note> result = service.update(note);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrorMessages());
        }
        return ResponseEntity.ok(result.getpayload());
    }

    // -------- DELETE --------
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable int id, @RequestParam int userId) {
        Result<Void> result = service.deleteById(id, userId);
        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrorMessages());
        }
        return ResponseEntity.noContent().build();
    }
}

