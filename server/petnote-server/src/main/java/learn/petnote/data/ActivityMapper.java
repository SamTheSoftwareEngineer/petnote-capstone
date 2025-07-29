package learn.petnote.data;
import learn.petnote.models.Activity;
import learn.petnote.models.ActivityName;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ActivityMapper implements RowMapper<Activity> {

    @Override
    public Activity mapRow(ResultSet rs, int rowNum) throws SQLException {
        Activity activity = new Activity();
        activity.setId(rs.getInt("id"));
        activity.setActivityDate(rs.getTimestamp("activityDate").toLocalDateTime());

        // Convert DB string to enum
        String activityNameStr = rs.getString("activityName");
        if (activityNameStr != null) {
            activity.setActivityName(ActivityName.valueOf(activityNameStr.toUpperCase()));
        }

        activity.setPetId(rs.getInt("petId"));
        activity.setUserId(rs.getInt("userId"));
        activity.setCompleted(rs.getBoolean("completed"));
        activity.setCreatedAt(rs.getTimestamp("createdAt").toLocalDateTime());
        return activity;
    }
}

