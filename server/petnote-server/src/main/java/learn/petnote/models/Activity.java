package learn.petnote.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Activity {
    private int id;

    @NotNull
    private LocalDateTime activityDate;

    @NotNull
    private ActivityName activityName;

    @NotNull
    private int petId;

    @NotNull
    private int userId;

    private boolean completed;

    private LocalDateTime createdAt;

    public Activity() {
    }

    public Activity(int id, LocalDateTime activityDate, ActivityName activityName, int petId, int userId, boolean completed, LocalDateTime createdAt) {
        this.id = id;
        this.activityDate = activityDate;
        this.activityName = activityName;
        this.petId = petId;
        this.userId = userId;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDateTime activityDate) {
        this.activityDate = activityDate;
    }

    public ActivityName getActivityName() {
        return activityName;
    }

    public void setActivityName(ActivityName activityName) {
        this.activityName = activityName;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
