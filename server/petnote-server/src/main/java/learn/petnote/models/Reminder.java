package learn.petnote.models;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reminder {
    private int id;
    private int userId;
    private int petId;
    private String message;
    private LocalDateTime remindAt;
    private boolean sent;

    public Reminder() {
    }

    public Reminder(int id, int userId, int petId, String message, LocalDateTime remindAt, boolean sent) {
        this.id = id;
        this.userId = userId;
        this.petId = petId;
        this.message = message;
        this.remindAt = remindAt;
        this.sent = sent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getRemindAt() {
        return remindAt;
    }

    public void setRemindAt(LocalDateTime remindAt) {
        this.remindAt = remindAt;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reminder reminder = (Reminder) o;
        return getId() == reminder.getId() && getUserId() == reminder.getUserId() && getPetId() == reminder.getPetId() && isSent() == reminder.isSent() && Objects.equals(getMessage(), reminder.getMessage()) && Objects.equals(getRemindAt(), reminder.getRemindAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUserId(), getPetId(), getMessage(), getRemindAt(), isSent());
    }
}

