package org.example.ProjetJavaEE.Ex.control;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

    // NOUVEAU : Affiche le formulaire d'inscription
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        // Ajouter un objet vide pour le formulaire si nécessaire (ex: new Professeur())
        // model.addAttribute("user", new Professeur());
        return "register";
    }

    // NOUVEAU : Traite l'inscription
    @PostMapping("/register")
    public String registerUser(
            @SuppressWarnings("unused") // Simule la réception des données
            Model model) {
        // Logique de validation et de sauvegarde dans la base de données
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        // professeurRepository.save(user);

        // Pour l'instant, cela redirige simplement vers la page de connexion
        return "redirect:/login?registered";
    }

}