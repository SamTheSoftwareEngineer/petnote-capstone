package learn.petnote.data;

import learn.petnote.models.Activity;

import java.util.List;

public interface ActivityRepository {
    Activity create(Activity activity);
    Activity findById(int id);
    List<Activity> findByPetId(int petId);
    List<Activity> findByUserId(int userId);
    boolean update(Activity activity);
    boolean deleteById(int id);
}
