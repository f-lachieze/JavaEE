package org.example.ProjetJavaEE.Ex.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    /**
     * Mappe l'URL racine "/" à la page d'accueil.
     * C'est le point d'entrée de l'application Web.
     */
    @GetMapping("/")
    public String home() {
        // Retourne le nom du template : src/main/resources/templates/index.html
        return "index";
    }

    // AJOUTER CETTE MÉTHODE POUR LA PAGE DE CONNEXION
    /**
     * Mappe l'URL "/login" à la page de connexion personnalisée.
     * Ceci est nécessaire pour que Spring MVC utilise le template login.html
     * au lieu du formulaire par défaut de Spring Security.
     */
    @GetMapping("/login")
    public String login() {
        // Retourne le nom du template : src/main/resources/templates/login.html
        return "login";
    }
}