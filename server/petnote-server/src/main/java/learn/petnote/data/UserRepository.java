package learn.petnote.data;

import learn.petnote.models.User;

public interface UserRepository {

    public User findByUsername(String username);

    public User createUser(User user);

}
