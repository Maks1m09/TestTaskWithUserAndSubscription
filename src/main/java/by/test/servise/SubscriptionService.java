package by.test.servise;

import by.test.entity.Subscription;
import by.test.entity.User;
import by.test.repository.SubscriptionRepository;
import by.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    private final UserRepository userRepository;

    public Subscription addSubscription(Long userId, Subscription subscription) {
        log.info("Attempting to add subscription for user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User not found with ID: {}", userId);
                    return new NoSuchElementException("User not found with ID: {}" + userId);
                });

        Subscription existingSubscription = subscriptionRepository.findByServiceName(subscription.getServiceName());
        if (existingSubscription == null) {
            existingSubscription = subscriptionRepository.save(subscription);
        }
        user.getSubscriptions().add(existingSubscription);
        userRepository.save(user);
        log.info("Successfully added subscription with ID: {} for user ID: {}", existingSubscription.getId(), userId);
        return existingSubscription;
    }

    public List<Subscription> getTopSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .sorted(Comparator.comparing((Subscription s) -> s.getUsers().size()).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }

    public List<Subscription> getSubscriptions(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        log.info("subscription was found and added to user with id: {}", userId);
        return new ArrayList<>(user.getSubscriptions());
    }

    public void deleteSubscription(Long userId, Long subId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            log.error("User with ID {} not found", userId);
            throw new RuntimeException("User not found by ID: " + userId);
        }
        User user = userOpt.get();
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(subId);
        if (!subscriptionOpt.isPresent()) {
            log.error("Attempted to delete non-existent subscription with ID: {}", subId);
            throw new RuntimeException("Subscription not found by ID: " + subId);
        }
        Subscription subscription = subscriptionOpt.get();
        if (!user.getSubscriptions().contains(subscription)) {
            log.error("User with ID {} attempted to delete a subscription with ID {} that does not belong to them", userId, subId);
            throw new RuntimeException("You do not have permission to delete this subscription");
        }
        user.getSubscriptions().remove(subscription);
        userRepository.save(user);
        log.info("Subscription with ID {} has been deleted for User with ID {}", subId, userId);
    }
}
