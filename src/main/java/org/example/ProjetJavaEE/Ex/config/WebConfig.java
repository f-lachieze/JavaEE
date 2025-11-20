package org.example.ProjetJavaEE.Ex.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Cette méthode enregistre un contrôleur de vue simple.
     * Elle mappe l'URL "/login" directement au template "login".
     * Spring Security utilise l'URL "/login" par défaut.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Mappe la requête GET /login au template login.html
        registry.addViewController("/login").setViewName("login");
    }
}