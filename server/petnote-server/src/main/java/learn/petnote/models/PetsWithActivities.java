package learn.petnote.models;

import java.util.List;

public class PetsWithActivities {
    private Pet pet;
    private List<Activity> activities;

    public PetsWithActivities(Pet pet, List<Activity> activities) {
        this.pet = pet;
        this.activities = activities;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }
}
