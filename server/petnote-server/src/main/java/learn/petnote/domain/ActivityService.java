package learn.petnote.domain;


import learn.petnote.data.ActivityRepository;
import learn.petnote.data.PetRepository;
import learn.petnote.models.Activity;
import learn.petnote.models.Pet;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ActivityService {
    private final ActivityRepository repository;
    private final PetRepository petRepository;

    public ActivityService(ActivityRepository repository, PetRepository petRepository) {
        this.repository = repository;
        this.petRepository = petRepository;
    }

    // ---------- Create ----------

    public Result<Activity> create(Activity activity) {
        Result<Activity> result = validate(activity, true);
        if (!result.isSuccess()) {
            return result;
        }

        // ensure the pet exists and belongs to the user
        Pet pet = petRepository.findById(activity.getPetId());
        if (pet == null) {
            result.addErrorMessage("Pet not found.", ResultType.NOT_FOUND);
            return result;
        }
        if (pet.getUserId() != activity.getUserId()) {
            result.addErrorMessage("Unauthorized: pet does not belong to user.", ResultType.INVALID);
            return result;
        }

        Activity created = repository.create(activity);
        if (created == null) {
            result.addErrorMessage("Failed to create activity.", ResultType.INVALID);
        } else {
            result.setpayload(created);
        }
        return result;
    }

    // ---------- Read ----------

    public Activity findById(int id) {
        return repository.findById(id);
    }

    public List<Activity> findByPetId(int petId) {
        return repository.findByPetId(petId);
    }

    public List<Activity> findByUserId(int userId) { return repository.findByUserId(userId); }

    // ---------- Update ----------

    public Result<Activity> update(Activity activity) {
        Result<Activity> result = validate(activity, false);
        if (!result.isSuccess()) {
            return result;
        }

        Activity existing = repository.findById(activity.getId());
        if (existing == null) {
            result.addErrorMessage("Activity not found.", ResultType.NOT_FOUND);
            return result;
        }

        // ownership check (use existing.petId to avoid tampering)
        Pet pet = petRepository.findById(existing.getPetId());
        if (pet == null || pet.getUserId() != activity.getUserId()) {
            result.addErrorMessage("Unauthorized: pet does not belong to user.", ResultType.INVALID);
            return result;
        }

        // force immutable fields to remain (if you want to lock them):
        activity.setPetId(existing.getPetId());
        activity.setUserId(existing.getUserId());

        boolean updated = repository.update(activity);
        if (!updated) {
            result.addErrorMessage("Update failed.", ResultType.INVALID);
        } else {
            result.setpayload(activity);
        }

        return result;
    }

    // ---------- Delete ----------

    public Result<Void> deleteById(int id, int userId) {
        Result<Void> result = new Result<>();

        Activity existing = repository.findById(id);
        if (existing == null) {
            result.addErrorMessage("Activity not found.", ResultType.NOT_FOUND);
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

    private Result<Activity> validate(Activity activity, boolean isCreate) {
        Result<Activity> result = new Result<>();

        if (activity == null) {
            result.addErrorMessage("Activity cannot be null.", ResultType.INVALID);
            return result;
        }

        if (!isCreate && activity.getId() <= 0) {
            result.addErrorMessage("Activity id is required for update.", ResultType.INVALID);
        }

        if (activity.getPetId() <= 0) {
            result.addErrorMessage("petId is required.", ResultType.INVALID);
        }

        if (activity.getUserId() <= 0) {
            result.addErrorMessage("userId is required.", ResultType.INVALID);
        }

        if (activity.getActivityName() == null) {
            result.addErrorMessage("activityName is required.", ResultType.INVALID);
        }

        if (activity.getActivityDate() == null) {
            result.addErrorMessage("activityDate is required.", ResultType.INVALID);
        }

        return result;
    }
}
