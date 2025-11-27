package org.example.ProjetJavaEE.Ex.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/";

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // L'ADMIN va à la page d'accueil par défaut (pour choisir)
            redirectUrl = "/";
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_GESTIONNAIRE"))) {
            // Le GESTIONNAIRE va à la page de réservation/calculs
            redirectUrl = "/"; // Ou une page de tableau de bord
        } else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_PROFESSOR"))) {
            // Le PROFESSEUR va directement à son emploi du temps
            redirectUrl = "/professor/timetable";
        }

        response.sendRedirect(redirectUrl);
    }
}