package learn.petnote.data;

import learn.petnote.models.Reminder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReminderJdbcClientRepositoryTest {

    @Autowired
    ReminderRepository repository;

    @Autowired
    private JdbcClient jdbcClient;

    @BeforeEach
    void setup() {
        jdbcClient.sql("call set_known_good_state();").update();
    }

    @Test
    void shouldAddReminder() {
        Reminder reminder = new Reminder();
        reminder.setUserId(1);
        reminder.setPetId(2);
        reminder.setMessage("Walk the dog");
        reminder.setRemindAt(LocalDateTime.now().plusHours(1));

        Reminder result = repository.add(reminder);

        assertNotNull(result);
        assertEquals("Walk the dog", result.getMessage());
    }

    @Test
    void shouldFindDueReminders() {
        // Add a reminder that is due now
        Reminder reminder = new Reminder();
        reminder.setUserId(1);
        reminder.setPetId(2);
        reminder.setMessage("Due reminder");
        reminder.setRemindAt(LocalDateTime.now());

        repository.add(reminder);

        List<Reminder> dueReminders = repository.findDueReminders(LocalDateTime.now().plusMinutes(1));

        assertFalse(dueReminders.isEmpty());
        assertEquals("Due reminder", dueReminders.get(0).getMessage());
    }

    @Test
    void shouldMarkAsSent() {
        Reminder reminder = new Reminder();
        reminder.setUserId(1);
        reminder.setPetId(2);
        reminder.setMessage("Send email");
        reminder.setRemindAt(LocalDateTime.now());

        repository.add(reminder);
        List<Reminder> due = repository.findDueReminders(LocalDateTime.now().plusSeconds(1));
        Reminder toSend = due.get(0);

        boolean updated = repository.markAsSent(toSend.getId());

        assertTrue(updated);

        List<Reminder> shouldBeEmpty = repository.findDueReminders(LocalDateTime.now());
        assertTrue(shouldBeEmpty.isEmpty());
    }

}