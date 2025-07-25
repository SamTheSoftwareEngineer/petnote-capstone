package learn.petnote.data;

import learn.petnote.models.Activity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ActivityJdbcClientRepository implements ActivityRepository {

    private final JdbcClient jdbcClient;

    public ActivityJdbcClientRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Activity create(Activity activity) {
        final String sql = """
                INSERT INTO activity (activityDate, activityName, petId, userId, completed)
                VALUES (:activityDate, :activityName, :petId, :userId, :completed);
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcClient.sql(sql)
                .param("activityDate", activity.getActivityDate())
                .param("activityName", activity.getActivityName().name()) // enum as string
                .param("petId", activity.getPetId())
                .param("userId", activity.getUserId())
                .param("completed", activity.isCompleted())
                .update(keyHolder, "id");

        activity.setId(keyHolder.getKey().intValue());
        return activity;
    }

    @Override
    public Activity findById(int id) {
        final String sql = "SELECT * FROM activity WHERE id = :id;";
        return jdbcClient.sql(sql)
                .param("id", id)
                .query(Activity.class)
                .optional()
                .orElse(null);
    }

    @Override
    public List<Activity> findByPetId(int petId) {
        final String sql = "SELECT * FROM activity WHERE petId = :petId;";
        return jdbcClient.sql(sql)
                .param("petId", petId)
                .query(Activity.class)
                .list();
    }

    @Override
    public List<Activity> findByUserId(int userId) {
        final String sql = "SELECT * FROM activity WHERE userId = :userId;";
        return jdbcClient.sql(sql)
                .param("userId", userId)
                .query(Activity.class)
                .list();
    }

    @Override
    public boolean update(Activity activity) {
        final String sql = """
                UPDATE activity
                SET activityDate = :activityDate,
                    activityName = :activityName,
                    completed = :completed
                WHERE id = :id;
                """;

        return jdbcClient.sql(sql)
                .param("activityDate", activity.getActivityDate())
                .param("activityName", activity.getActivityName().name())
                .param("completed", activity.isCompleted())
                .param("id", activity.getId())
                .update() > 0;
    }

    @Override
    public boolean deleteById(int id) {
        final String sql = "DELETE FROM activity WHERE id = :id;";
        return jdbcClient.sql(sql)
                .param("id", id)
                .update() > 0;
    }
}

