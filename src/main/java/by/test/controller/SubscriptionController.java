package by.test.controller;

import by.test.entity.Subscription;
import by.test.servise.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{userId}/subscriptions")
    public ResponseEntity<Subscription> addSubscriptionToUser(@PathVariable("userId") Long userId, @RequestBody Subscription subscription) {
        return ResponseEntity.ok(subscriptionService.addSubscription(userId, subscription));
    }

    @GetMapping("/{userId}/subscriptions")
    public ResponseEntity<List<Subscription>> getUserSubscription(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptions(userId));
    }

    @DeleteMapping("/{userId}/subscriptions/{sub_id}")
    public ResponseEntity<Void> deleteSubscriptionFromUser(@PathVariable("userId") Long userId,
                                                           @PathVariable("sub_id") Long subId) {
        subscriptionService.deleteSubscription(userId, subId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/subscriptions/top")
    public ResponseEntity<List<Subscription>> getTopSubscriptions() {
        List<Subscription> topSubscriptions = subscriptionService.getTopSubscriptions();
        return ResponseEntity.ok(topSubscriptions);
    }
}
