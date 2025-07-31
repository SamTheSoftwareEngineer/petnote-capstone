package learn.petnote.data;

import learn.petnote.models.Reminder;

import java.time.LocalDateTime;
import java.util.List;

public interface ReminderRepository {
    Reminder add(Reminder reminder);
    List<Reminder> findDueReminders(LocalDateTime now);
    List<Reminder> findAll();
    List<Reminder> findByUserId(int userId);
    List<Reminder> findByPetId(int petId);
    boolean markAsSent(int id);
    String findUserEmailById(int userId);
    boolean update(Reminder reminder);
    Reminder findById(int id);
    boolean deleteById(int id);
}
