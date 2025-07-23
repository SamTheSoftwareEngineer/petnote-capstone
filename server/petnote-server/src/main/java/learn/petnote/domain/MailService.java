package learn.petnote.domain;

import learn.petnote.models.User;


public interface MailService {
    void sendVerificationEmail(User user);
}
