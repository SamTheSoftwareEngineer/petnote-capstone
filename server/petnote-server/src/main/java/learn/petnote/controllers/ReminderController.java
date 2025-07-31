package learn.petnote.controllers;

import learn.petnote.domain.ReminderService;
import learn.petnote.models.Reminder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = {"http://localhost:5173"}) // update if your frontend domain is different
public class ReminderController {

    private final ReminderService service;

    public ReminderController(ReminderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Reminder> create(@RequestBody Reminder reminder) {
        System.out.println("Incoming reminder " + reminder.getMessage());
        Reminder result = service.add(reminder);
        if (result == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/due")
    public ResponseEntity<List<Reminder>> findDueReminders() {
        List<Reminder> dueReminders = service.findDueReminders(LocalDateTime.now());
        return ResponseEntity.ok(dueReminders);
    }

    @PutMapping("/sent/{id}")
    public ResponseEntity<Void> markAsSent(@PathVariable int id) {
        boolean success = service.markAsSent(id);
        return success ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reminder>> findByUser(@PathVariable int userId) {
        List<Reminder> reminders = service.findByUserId(userId);
        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Reminder>> findAllRemindersForPet(@PathVariable int petId) {
        List<Reminder> reminders = service.findAllRemindersByPetId(petId);

        if (reminders == null || reminders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(reminders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reminder> findById(@PathVariable int id) {
        Reminder reminder = service.findById(id);
        return reminder != null ? ResponseEntity.ok(reminder) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable int id, @RequestBody Reminder reminder) {
        if (id != reminder.getId()) {
            return ResponseEntity.badRequest().build();
        }
        boolean success = service.update(reminder);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        boolean success = service.deleteById(id);
        return success ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

