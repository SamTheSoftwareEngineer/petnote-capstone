package learn.petnote.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class Pet {
    @NotNull(message = "Pet id must be present ")
    private int id;
    private String profilePictureURL;
    @NotBlank(message = "Pet name cannot be blank")
    private String petName;
    private String breed;
    private String species;
    private int age;
    private float weight;
    private int userId;
    private LocalDateTime createdAt;

    public Pet() {
    }

    public Pet(int id, String profilePictureURL, String petName, String breed, String species, int age, float weight, int userId, LocalDateTime createdAt) {
        this.id = id;
        this.profilePictureURL = profilePictureURL;
        this.petName = petName;
        this.breed = breed;
        this.species = species;
        this.age = age;
        this.weight = weight;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
