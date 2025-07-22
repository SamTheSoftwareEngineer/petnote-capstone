package learn.petnote.domain;

import learn.petnote.data.UserRepository;
import learn.petnote.models.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceTest {

    @Autowired
    UserService service;

    @MockBean
    UserRepository repository;


    @Test
    void createHappyPath() {
        User toCreate = new User(0, "username", "email@email.com", "password");
        Result<User> expected = new Result<>();
        User afterCreate = new User(1, "username", "email@email.com", "password");
        expected.setpayload(afterCreate);
        when(repository.createUser(toCreate)).thenReturn(afterCreate);

        Result<User> actual = service.createUser(toCreate);

        assertEquals(expected, actual);
    }

    @Test
    void createFailsWhenUsernameIsBlank() {
        User toCreate = new User(0, "", "testemail@email.com", "password");
        Result<User> expected = new Result<>();
        expected.addErrorMessage("Username cannot be blank", ResultType.INVALID);

        Result<User> actual = service.createUser(toCreate);

        assertEquals(expected, actual);
    }

    @Test
    void createFailsWhenPasswordIsBlank() {
        User toCreate = new User(0, "username", "testemail@email.com", "");
        Result<User> expected = new Result<>();
        expected.addErrorMessage("Password cannot be blank", ResultType.INVALID);

        Result<User> actual = service.createUser(toCreate);

        assertEquals(expected, actual);
    }

    @Test
    void createFailsWhenEmailIsBlank() {
        User toCreate = new User(0, "username", "", "password");
        Result<User> expected = new Result<>();
        expected.addErrorMessage("Email cannot be blank", ResultType.INVALID);

        Result<User> actual = service.createUser(toCreate);

        assertEquals(expected, actual);
    }

    @Nested
    class Authenticate {
        @Test
        void userNotFound() {
            String nonExistingUsername = "zzz";
            when(repository.findByUsername(nonExistingUsername)).thenReturn(null);
            Result<User> expected = new Result<>();
            expected.addErrorMessage("User not found", ResultType.NOT_FOUND);

            Result<User> actual = service.authenticate(nonExistingUsername, "password");

            assertEquals(expected, actual);
        }

        @Test
        void userExistsButPasswordIncorrect() {
            String username = "username";
            when(repository.findByUsername(username)).thenReturn(new User(1, username, "email@email.com", "password"));
            Result<User> expected = new Result<>();
            expected.addErrorMessage("Incorrect password", ResultType.INVALID);

            Result<User> actual = service.authenticate(username, "wrongPassword");

            assertEquals(expected, actual);
        }

        @Test
        void userExistsAndPasswordCorrectAndEmailPresent() {
            User user = new User(1, "username","test@test.com", "password");
            when(repository.findByUsername(user.getUsername())).thenReturn(user);
            Result<User> expected = new Result<>();
            expected.setpayload(user);

            Result<User> actual = service.authenticate(user.getUsername(), user.getPassword());

            assertEquals(expected, actual);
        }
    }
}