package learn.petnote.models;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Note {
    @NotNull
    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime editedAt;

    @NotNull
    private String description;

    @NotNull
    private int petId;

    @NotNull
    private int userId;

    public Note() {
    }

    public Note(int id, LocalDateTime createdAt, LocalDateTime editedAt, String description, int petId, int userId) {
        this.id = id;
        this.createdAt = createdAt;
        this.editedAt = editedAt;
        this.description = description;
        this.petId = petId;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getEditedAt() {
        return editedAt;
    }

    public void setEditedAt(LocalDateTime editedAt) {
        this.editedAt = editedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }
}
