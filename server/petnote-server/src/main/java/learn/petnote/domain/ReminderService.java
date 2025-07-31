package learn.petnote.domain;

import learn.petnote.data.PetRepository;
import learn.petnote.data.ReminderRepository;
import learn.petnote.data.UserRepository;
import learn.petnote.models.Pet;
import learn.petnote.models.Reminder;
import learn.petnote.models.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository repository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final GmailMailService mailService;

    public ReminderService(ReminderRepository repository,
                           UserRepository userRepository,
                           PetRepository petRepository,
                           GmailMailService mailService) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.petRepository = petRepository;
        this.mailService = mailService;
    }

    public List<Reminder> findAll() {
        return repository.findAll();
    }

    public List<Reminder> findDueReminders(LocalDateTime now) {
        return repository.findDueReminders(now);
    }

    public List<Reminder> findByUserId(int userId) {
        return repository.findByUserId(userId);
    }

    public boolean markAsSent(int id) {
        return repository.markAsSent(id);
    }

    String findUserEmailById(int userId) {
        return repository.findUserEmailById(userId);
    }

    public Reminder findById(int reminderId) {
        return repository.findById(reminderId);
    }

    public Reminder add(Reminder reminder) {
        return repository.add(reminder);
    }

    public boolean update(Reminder reminder) {
        return repository.update(reminder);
    }

    public boolean deleteById(int reminderId) {
        return repository.deleteById(reminderId);
    }

    public List<Reminder> findAllRemindersByPetId(int petId) {
        return repository.findByPetId(petId);
    }

    /**
     * Runs every minute and checks if any reminders are due.
     */
    @Scheduled(cron = "0 * * * * *") // Every minute
    public void sendDueReminders() {
        LocalDateTime now = LocalDateTime.now();
        List<Reminder> dueReminders = repository.findDueReminders(now);

        for (Reminder reminder : dueReminders) {
            User user = userRepository.findById(reminder.getUserId());
            Pet pet = petRepository.findById(reminder.getPetId());

            if (user != null && pet != null) {
                try {
                    mailService.sendReminderEmail(
                            user.getEmail(),
                            pet.getPetName(),
                            reminder.getMessage(),
                            reminder.getRemindAt()
                    );
                    boolean updated = repository.markAsSent(reminder.getId());

                    if (updated) {
                        System.out.println("✅ Sent reminder email to " + user.getEmail() + " for pet " + pet.getPetName());
                    } else {
                        System.err.println("⚠️ Failed to mark reminder ID " + reminder.getId() + " as sent.");
                    }
                } catch (Exception e) {
                    System.out.println("❌ Failed to send reminder for user " + user.getEmail() + ": " + e.getMessage());
                }
            }
        }
    }
}

