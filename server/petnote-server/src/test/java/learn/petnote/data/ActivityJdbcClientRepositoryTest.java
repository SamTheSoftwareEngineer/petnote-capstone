package learn.petnote.data;

import learn.petnote.models.Activity;
import learn.petnote.models.ActivityName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ActivityJdbcClientRepositoryTest {

    @Autowired
    private ActivityJdbcClientRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void create_shouldAssignIdAndRoundTrip() {
        Activity toCreate = makeValid();

        Activity created = repository.create(toCreate);

        assertNotNull(created);
        assertTrue(created.getId() > 0);

        Activity fetched = repository.findById(created.getId());
        assertNotNull(fetched);
        assertEquals(toCreate.getActivityName(), fetched.getActivityName());
        assertEquals(toCreate.getPetId(), fetched.getPetId());
        assertEquals(toCreate.getUserId(), fetched.getUserId());
        assertEquals(toCreate.isCompleted(), fetched.isCompleted());
    }

    @Test
    void findById_notFound_returnsNull() {
        Activity activity = repository.findById(10000000);
        assertNull(activity);
    }

    @Test
    void findByPetId_shouldReturnList() {
        List<Activity> activities = repository.findByPetId(1);
        assertNotNull(activities);
    }

    @Test
    void findByUserId_shouldReturnList() {
        List<Activity> activities = repository.findByUserId(1);
        assertNotNull(activities);
    }

    @Test
    void update_existing_returnsTrueAndPersists() {
        Activity toCreate = makeValid();
        Activity created = repository.create(toCreate);

        created.setActivityName(ActivityName.FEED);
        created.setCompleted(false);

        boolean updated = repository.update(created);
        assertTrue(updated);

        Activity fetched = repository.findById(created.getId());
        assertNotNull(fetched);
        assertEquals(ActivityName.FEED, fetched.getActivityName());
        assertFalse(fetched.isCompleted());
    }

    @Test
    void update_nonExisting_returnsFalse() {
        Activity a = makeValid();
        a.setId(999999); // non-existent
        boolean updated = repository.update(a);
        assertFalse(updated);
    }

    @Test
    void delete_existing_returnsTrueThenGone() {
        Activity toCreate = makeValid();
        Activity created = repository.create(toCreate);

        boolean deleted = repository.deleteById(created.getId());
        assertTrue(deleted);

        Activity fetched = repository.findById(created.getId());
        assertNull(fetched);
    }

    @Test
    void delete_missing_returnsFalse() {
        boolean deleted = repository.deleteById(123456789);
        assertFalse(deleted);
    }


    //    Helper method
    private Activity makeValid() {
        Activity a = new Activity();
        a.setActivityDate(LocalDateTime.now().minusHours(1));
        a.setActivityName(ActivityName.WALK);
        a.setPetId(1);
        a.setUserId(1);
        a.setCompleted(true);
        return a;
    }
}