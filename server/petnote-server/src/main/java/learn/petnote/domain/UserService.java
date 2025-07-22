package learn.petnote.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import learn.petnote.data.UserRepository;
import learn.petnote.models.User;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Result<User> createUser(User user) {
        Result<User> result = new Result<>();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<User> violation : violations) {
                result.addErrorMessage(violation.getMessage(), ResultType.INVALID);
            }
        }

        if (result.isSuccess()) {
            User created = repository.createUser(user);
            result.setpayload(created);
        }

        return result;
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Result<User> authenticate(String username, String password) {
        Result<User> result = new Result<>();
        User user = repository.findByUsername(username);

        if (user == null) {
            result.addErrorMessage("User not found", ResultType.NOT_FOUND);
            return result;
        }

        if (user.getPassword().equals(password)) {
            result.setpayload(user);
        } else {
            result.addErrorMessage("Incorrect password", ResultType.INVALID);
        }

        return result;
    }
}

