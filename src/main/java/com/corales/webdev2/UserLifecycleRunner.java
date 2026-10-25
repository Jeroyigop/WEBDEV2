package com.corales.webdev2;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserLifecycleRunner implements CommandLineRunner {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        User user = new User();
        user.setUsername("week7_user");
        user.setEmail("week7@example.com");

        entityManager.persist(user);
        entityManager.flush();
        System.out.println("User persisted; managed: " + entityManager.contains(user));

        entityManager.detach(user);
        System.out.println("User detached; managed: " + entityManager.contains(user));

        user.setEmail("unsaved@example.com");
        System.out.println("Detached user's in-memory email changed to: " + user.getEmail());
        System.out.println("No merge performed; the database email remains week7@example.com.");
    }
}
