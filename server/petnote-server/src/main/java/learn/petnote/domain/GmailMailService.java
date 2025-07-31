package learn.petnote.domain;

import learn.petnote.models.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class GmailMailService implements MailService {

    private final JavaMailSender mailSender;

    public GmailMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(User user) {
        String to = user.getEmail();
        String username = user.getUsername();
        System.out.println("Sending verification email to: " + user.getEmail());
        System.out.println("Verification URL: http://localhost:5173/verify?token=" + user.getVerificationToken());
        String verifyUrl = "http://localhost:5173/verify?token=" + user.getVerificationToken();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("PetNote: Verify your email");
        message.setText("Hi " + username + ",\n\nPlease verify your account by clicking the link below:\n\n"
                + verifyUrl + "\n\nThanks!\n- The PetNote Team");

        mailSender.send(message);
    }

    public void sendReminderEmail(String to, String petName, String messageText, LocalDateTime dueDateTime) {
        String subject = "PetNote Reminder: " + messageText;

        String body = String.format("""
        Hi there,

        This is a reminder for your pet *%s*:
        %s

        Scheduled for: %s

        Thanks for using PetNote!
        """, petName, messageText, dueDateTime.format(DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm")));

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }


}

