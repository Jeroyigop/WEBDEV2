
package com.corales.webdev2;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class UserLifecycleRunner implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {

        // STATE 1: Transient
        User user = new User();
        user.setUsername("week7_user");
        user.setEmail("week7@example.com");
        user.setCreatedAt(LocalDateTime.now());

        System.out.println("\n===== JPA ENTITY LIFECYCLE =====");
        System.out.println("STATE 1 - Transient: User object created.");

        // STATE 2: Managed
        entityManager.persist(user);
        entityManager.flush();

        System.out.println("STATE 2 - Managed: User saved.");
        System.out.println("Generated ID: " + user.getId());

        // STATE 3: Detached
        entityManager.detach(user);

        System.out.println("STATE 3 - Detached: User removed from persistence context.");

        // STATE 4: Modify the detached object
        user.setEmail("changed@example.com");

        System.out.println("STATE 4 - Detached object modified.");
        System.out.println("Object email: " + user.getEmail());
        System.out.println("No merge() called; database email should remain week7@example.com.");
        System.out.println("================================\n");
    }
}
