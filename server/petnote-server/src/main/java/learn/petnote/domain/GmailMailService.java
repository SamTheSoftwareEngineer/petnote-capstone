package learn.petnote.domain;

import learn.petnote.models.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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
}

