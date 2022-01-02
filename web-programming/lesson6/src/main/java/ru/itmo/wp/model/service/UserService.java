package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

/** @noinspection UnstableApiUsage*/
public class UserService {
    private final UserRepository userRepository = new UserRepositoryImpl();
    private static final String PASSWORD_SALT = "177d4b5f2e4f4edafa7404533973c04c513ac619";

    public void validateRegistration(User user, String password, String passwordConfirmation) throws ValidationException {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-]" +
                "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        validate(user.getEmail(), "Email", emailRegex, 0, 255);
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email is already in use");
        }

        String loginRegex = "[a-z]+";
        validate(user.getLogin(), "Login", loginRegex, 0, 8);
        if (userRepository.findByLogin(user.getLogin()) != null) {
            throw new ValidationException("Login is already in use");
        }

        String passwordRegex = "[a-z0-9]+";
        validate(password, "password", passwordRegex, 4, 12);
        if (!password.equals(passwordConfirmation)) {
            throw new ValidationException("Password and passwordConfirmation not equals");
        }
    }

    private void validate(String field, String fieldName, String regex, int minLength, int maxLength)
            throws ValidationException {
        if (Strings.isNullOrEmpty(field)) {
            throw new ValidationException(fieldName + " is required");
        }
        if (!field.matches(regex)) {
            throw new ValidationException("Invalid " + fieldName);
        }
        if (field.length() < minLength) {
            throw new ValidationException(fieldName + " can't be shorter than " + minLength + " characters");
        }
        if (field.length() > maxLength) {
            throw new ValidationException(fieldName + " can't be longer than " + maxLength +  "characters");
        }
    }

    public void register(User user, String password) {
        userRepository.save(user, getPasswordSha(password));
    }

    private String getPasswordSha(String password) {
        return Hashing.sha256().hashBytes((PASSWORD_SALT + password).getBytes(StandardCharsets.UTF_8)).toString();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean validateEnter(String loginOrEmail, String password) throws ValidationException {
        String passwordSha = getPasswordSha(password);
        User user = userRepository.findByLoginAndPasswordSha(loginOrEmail, passwordSha);
        if (user == null) {
            user = userRepository.findByEmailAndPasswordSha(loginOrEmail, passwordSha);
            if (user == null) {
                throw new ValidationException("Invalid loginOrEmail or password");
            }
            return false;
        } else {
            return true;
        }
    }

    public User findByLoginAndPassword(String login, String password) {
        return userRepository.findByLoginAndPasswordSha(login, getPasswordSha(password));
    }

    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPasswordSha(email, getPasswordSha(password));
    }

    public int findCount() {
        return userRepository.findCount();
    }

    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
}
