package learn.petnote.data;

import learn.petnote.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserJdbcClientRepositoryTest {

    @Autowired
    private UserJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void createUser() {
        User toCreate = new User(0, "username", "user1@email.com", "password");
        User expected = new User(4, "username", "user1@email.com", "password");

        User actual = repository.createUser(toCreate);

        assertEquals(expected, actual);
        assertNotNull(repository.findByUsername("username"));
    }

    @Test
    void whenUserExists() {
        User user = repository.findByUsername("user1");
        assertNotNull(user);
    }

    @Test
    void whenUserDoesNotExist() {
        User user = repository.findByUsername("samaira");
        assertNull(user);
    }
}


