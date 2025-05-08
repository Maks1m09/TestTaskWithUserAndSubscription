package by.test.servise;

import by.test.entity.User;
import by.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User createUser(User user) {
        if (user.getId() != null && userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists.");
        }
        log.info(user + "was created");
        return userRepository.save(user);
    }

    public Optional<User> getUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info(id + ": user was found");
        } else {
            log.warn(id + ": user not found");
        }
        return user;
    }

    public User updateUser(Long id, User userDetails) {
        return userRepository.findById(id).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            log.info("User with ID {}: updated", id);
            return userRepository.save(user);
        }).orElseThrow(() -> {
            log.error("User not found with ID: {}", id);
            return new RuntimeException("User not found with ID: {}" + id);
        });
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.info("User not found with ID: {}", id);
            throw new RuntimeException("User not found with ID: " + id);
        }
        log.info("User with" + id + ": was deleted");
        userRepository.deleteById(id);
    }
}

