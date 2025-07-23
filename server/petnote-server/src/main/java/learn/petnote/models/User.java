package learn.petnote.models;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {
    private int id;
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Email cannot be blank")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    private String password;
    private String profilePictureURL;
    private boolean isVerified;
    private String verificationToken;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(int id, String username, String email, String password, String profilePictureURL, boolean isVerified, String verificationToken, LocalDateTime createdAt) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.profilePictureURL = profilePictureURL;
        this.isVerified = isVerified;
        this.verificationToken = verificationToken;
        this.createdAt = createdAt;
    }

    public User(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePictureURL() {
        return profilePictureURL;
    }

    public void setProfilePictureURL(String profilePictureURL) {
        this.profilePictureURL = profilePictureURL;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && isVerified() == user.isVerified() && Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getPassword(), user.getPassword()) && Objects.equals(getProfilePictureURL(), user.getProfilePictureURL()) && Objects.equals(getCreatedAt(), user.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getEmail(), getPassword(), getProfilePictureURL(), isVerified(), getCreatedAt());
    }


}
