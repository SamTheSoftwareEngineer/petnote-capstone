package learn.petnote.controllers;

import learn.petnote.domain.ActivityService;
import learn.petnote.domain.Result;
import learn.petnote.models.Activity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/activities")
@CrossOrigin(origins = "http://localhost:5173")
public class ActivityController {

    private final ActivityService service;

    public ActivityController(ActivityService service) {
        this.service = service;
    }

    // Create
    @PostMapping
    public ResponseEntity<Object> create (@RequestBody Activity activity) {
        Result<Activity> result = service.create(activity);

        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
        }
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable int id) {
        Activity activity = service.findById(id);

        return activity == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(activity);
    }

    // Get all activities for a pet
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Activity>> findByPetId(@PathVariable int petId) {
        List<Activity> activities = service.findByPetId(petId);

        if (activities == null || activities.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activities);
    }

    // -------- UPDATE --------
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @RequestBody Activity activity) {
        if (id != activity.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Path id and activity id must match.");
        }

        Result<Activity> result = service.update(activity);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(result.getpayload());
    }

    // -------- DELETE --------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id, @RequestParam int userId) {
        Result<Void> result = service.deleteById(id, userId);
        if (!result.isSuccess()) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return ResponseEntity.noContent().build();
    }
}

