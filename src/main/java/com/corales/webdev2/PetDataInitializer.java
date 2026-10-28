package com.corales.webdev2;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class PetDataInitializer implements ApplicationRunner {

    private final PetRepository petRepository;

    public PetDataInitializer(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (petRepository.count() == 0) {
            petRepository.save(new Pet("CHIP-0001", "Luna", "Cat", "Siamese"));
        }
    }
}
