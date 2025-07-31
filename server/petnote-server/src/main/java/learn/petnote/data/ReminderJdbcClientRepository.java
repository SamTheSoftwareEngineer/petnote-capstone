package learn.petnote.data;

import learn.petnote.models.Reminder;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReminderJdbcClientRepository implements ReminderRepository {

    private final JdbcClient jdbcClient;

    public ReminderJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Reminder add(Reminder reminder) {
        final String sql = """
            INSERT INTO reminder (userId, petId, message, remindAt)
            VALUES (:userId, :petId, :message, :remindAt)
        """;

        jdbcClient.sql(sql)
                .param("userId", reminder.getUserId())
                .param("petId", reminder.getPetId())
                .param("message", reminder.getMessage())
                .param("remindAt", reminder.getRemindAt())
                .update();

        return reminder;
    }

    @Override
    public List<Reminder> findDueReminders(LocalDateTime now) {
        final String sql = """
            SELECT * FROM reminder
            WHERE remindAt <= :now AND sent = false
        """;

        return jdbcClient.sql(sql)
                .param("now", now)
                .query(Reminder.class)
                .list();
    }

    @Override
    public List<Reminder> findAll() {
        final String sql = """
                select * from reminder;""";

        return jdbcClient.sql(sql)
                .query(Reminder.class)
                .list();
    }

    @Override
    public List<Reminder> findByUserId(int userId) {
        final String sql = """
                select * from reminder where userId = :userId;""";

        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(Reminder.class)
                .list();
    }

    @Override
    public List<Reminder> findByPetId(int petId) {
        final String sql = """
                select * from reminder where petId = :petId;""";

        return jdbcClient.sql(sql)
                .param("petId", petId)
                .query(Reminder.class)
                .list();
    }


    @Override
    public boolean markAsSent(int id) {
        final String sql = """
            UPDATE reminder SET sent = true WHERE id = :id
        """;

        return jdbcClient.sql(sql)
                .param("id", id)
                .update() > 0;
    }

    @Override
    public String findUserEmailById(int userId) {
        final String sql = """
            SELECT email FROM user WHERE id = :userId
        """;

        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(String.class)
                .single();
    }

    @Override
    public boolean update(Reminder reminder) {
        final String sql = """
                update reminder set
                message = :message,
                remindAt = :remindAt
                where id = :id
                """;

        return jdbcClient.sql(sql)
                .param("message", reminder.getMessage())
                .param("remindAt", reminder.getRemindAt())
                .param("id", reminder.getId())
                .update() > 0;
    }

    @Override
    public Reminder findById(int id) {
        final String sql = """
                select * from reminder where id = :id;""";

        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Reminder.class)
                .optional()
                .orElse(null);

    }

    @Override
    public boolean deleteById(int id) {
        final String sql = """
                delete from reminder where id = :id;""";

        return jdbcClient.sql(sql)
                .param("id", id)
                .update() > 0;
    }

}

