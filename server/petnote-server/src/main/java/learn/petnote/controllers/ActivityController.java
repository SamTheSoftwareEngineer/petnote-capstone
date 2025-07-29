package learn.petnote.controllers;

import learn.petnote.domain.ActivityService;
import learn.petnote.domain.Result;
import learn.petnote.models.Activity;
import learn.petnote.models.ActivityName;
import learn.petnote.models.PetsWithActivities;
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

    // Get activity types
    @GetMapping("/types")
    public ActivityName[] getActivityNames() {
        return ActivityName.values();
    }

    // -------- CREATE --------
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Activity activity) {
        Result<Activity> result = service.create(activity);

        if (!result.isSuccess()) {
            return ResponseEntity.badRequest().body(result.getErrorMessages());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(result.getpayload());
    }

    // -------- READ --------

    @GetMapping("/{id}")
    public ResponseEntity<Activity> findById(@PathVariable int id) {
        Activity activity = service.findById(id);
        return activity == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(activity);
    }

    // Get all activities for a specific pet
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Activity>> findByPetId(@PathVariable int petId) {
        List<Activity> activities = service.findByPetId(petId);
        // Return 200 OK with an empty list (more standard REST behavior)
        return ResponseEntity.ok(activities);
    }

    // Get all activities for all pets that belong to a specific user
    @GetMapping("/user/{userId}/pets-with-activities")
    public List<PetsWithActivities> getPetsWithActivities(@PathVariable int userId) {
        return service.findPetsAndActivitiesByUserId(userId);
    }


    // -------- UPDATE --------
    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable int id, @RequestBody Activity activity) {
        if (id != activity.getId()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Path ID and activity ID must match.");
        }

        Result<Activity> result = service.update(activity);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(result.getErrorMessages());
        }
        return ResponseEntity.noContent().build();
    }
}


