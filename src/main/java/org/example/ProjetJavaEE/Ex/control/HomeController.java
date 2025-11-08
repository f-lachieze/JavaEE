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
}