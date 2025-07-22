package learn.petnote.data;

import learn.petnote.models.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcClientRepository implements UserRepository{
    private final JdbcClient jdbcClient;

    public UserJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public User findByUsername(String username) {
        final String sql = """
                select * from `user` where username = :username;""";

        return jdbcClient.sql(sql)
                .param("username", username)
                .query(User.class)
                .optional().orElse(null);
    }


    @Override
    public User createUser(User user) {
        final String sql = """
                insert into `user` (username, email, `password`)
                values (:username, :email, :password)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcClient.sql(sql)
                .param("username", user.getUsername())
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .update(keyHolder, "id");

        if (rowsAffected == 0) {
            return null;
        }

        user.setId(keyHolder.getKey().intValue());

        return user;
    }

    @Override
    public User findByEmail(String email) {
        final String sql = """
                select * from `user` where email = :email""";

        return jdbcClient.sql(sql)
                .param("email", email)
                .query(User.class)
                .optional().orElse(null);
    }

    }

