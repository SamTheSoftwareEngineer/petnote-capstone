package learn.petnote.domain;

import learn.petnote.data.ReminderRepository;
import learn.petnote.models.Reminder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class ReminderServiceTest {

    @Autowired
    ReminderService service;

    @MockBean
    ReminderRepository repository;

    @Test
    void shouldAddReminder() {
        Reminder input = new Reminder(0, 1, 2, "Vet appointment", LocalDateTime.now().plusDays(1), false);
        Reminder expected = new Reminder(0, 1, 2, "Vet appointment", input.getRemindAt(), false);

        when(repository.add(input)).thenReturn(expected);

        Reminder actual = service.add(input);
        assertEquals(expected, actual);
    }

    @Test
    void shouldFindDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        Reminder r1 = new Reminder(1, 1, 1, "Feed the fish", now.minusMinutes(5), false);
        Reminder r2 = new Reminder(2, 2, 1, "Walk the dog", now.minusMinutes(1), false);

        when(repository.findDueReminders(now)).thenReturn(List.of(r1, r2));

        List<Reminder> result = service.findDueReminders(now);
        assertEquals(2, result.size());
        assertTrue(result.contains(r1));
        assertTrue(result.contains(r2));
    }

    @Test
    void shouldMarkReminderAsSent() {
        when(repository.markAsSent(1)).thenReturn(true);

        assertTrue(service.markAsSent(1));
    }

    @Test
    void shouldFindUserEmailById() {
        when(repository.findUserEmailById(99)).thenReturn("sam@example.com");

        String result = service.findUserEmailById(99);
        assertEquals("sam@example.com", result);
    }

}