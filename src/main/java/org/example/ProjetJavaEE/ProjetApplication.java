package org.example.ProjetJavaEE;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

// Assurez-vous d'importer vos Repositories ici quand vous les créerez
// import org.example.ProjetJavaEE.repository.VotreEntiteRepository;

@SpringBootApplication
public class ProjetApplication implements CommandLineRunner {

    // 1. Point d'injection pour le Repository (à décommenter/adapter plus tard)
    // @Autowired
    // private VotreEntiteRepository votreEntiteRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProjetApplication.class, args);
    }

    /**
     * Cette méthode sera exécutée automatiquement au démarrage de l'application Spring Boot.
     * C'est ici que vous ferez les appels à la base de données.
     */
    @Override
    public void run(String... args) throws Exception {
        System.out.println("--- Démarrage de l'application et accès à la BDD ---");

        // 2. Exemple d'utilisation du Repository (à implémenter après la création des Repositories)
        // System.out.println("Nombre d'enregistrements : " + votreEntiteRepository.count());

        // Vous ferez ici les appels pour les opérations demandées par le TP (insertion, sélection, etc.)

        System.out.println("--- Exécution du CommandLineRunner terminée ---");
    }
}