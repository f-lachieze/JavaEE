package org.example.ProjetJavaEE.Ex.control;

import org.example.ProjetJavaEE.Ex.domain.ProfesseurRepository; // ⬅️ NOUVEL IMPORT
import org.example.ProjetJavaEE.Ex.modele.Professeur;             // ⬅️ NOUVEL IMPORT
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // ⬅️ NOUVEL IMPORT
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.example.ProjetJavaEE.Ex.domain.RoleRepository;
import org.example.ProjetJavaEE.Ex.modele.Role;

import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    // Injection des outils JPA et Sécurité
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Autowired
    private RoleRepository roleRepository;


    /**
     * Mappe l'URL racine "/" à la page d'accueil.
     * C'est le point d'entrée de l'application Web.
     */
    @GetMapping("/")
    public String home() {
        // Retourne le nom du template : src/main/resources/templates/index.html
        return "index";
    }


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
        model.addAttribute("errorMessage", null);
        return "register";
    }

    // NOUVEAU : Traite l'inscription
//    @PostMapping("/register")
   // public String registerUser(


            // @SuppressWarnings("unused") // Simule la réception des données
           // Model model) {
        // Logique de validation et de sauvegarde dans la base de données
        // user.setPassword(passwordEncoder.encode(user.getPassword()));
        // professeurRepository.save(user);

        // Pour l'instant, cela redirige simplement vers la page de connexion
       // return "redirect:/login?registered";
 //   )
  //  {}


    // ⬅️ LOGIQUE D'ENREGISTREMENT MISE À JOUR : Traite la soumission du formulaire
    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam(required = false) String nom, // Ajout du champ optionnel 'nom'
            Model model) {

        try {
            // 1. Vérification de l'existence de l'utilisateur
            if (professeurRepository.findByUsername(username).isPresent()) {
                model.addAttribute("errorMessage", "Ce nom d'utilisateur est déjà pris.");
                return "register"; // Reste sur la page d'inscription avec erreur
            }

            // 2. RÉCUPÉRATION DE L'ENTITÉ ROLE_PROFESSOR PERSISTANTE
            Optional<Role> profRoleOptional = roleRepository.findByName("ROLE_PROFESSOR");
            if (profRoleOptional.isEmpty()) {
                throw new IllegalStateException("Le rôle de PROFESSOR n'est pas initialisé. Redémarrez l'application.");
            }
            Role profRole = profRoleOptional.get();

            // 3. Hachage du mot de passe
            String encodedPassword = passwordEncoder.encode(password);

            // 4. Création de la nouvelle entité Professeur
            Professeur newProfesseur = new Professeur(
                    username,
                    encodedPassword,
                    nom,
                    Set.of(profRole) // ⬅️ CORRECTION : Passer le rôle dans un Set
            );

            // 5. Sauvegarde dans la base de données (persistance)
            professeurRepository.save(newProfesseur);

            // 6. Redirection vers la page de connexion avec un message de succès
            return "redirect:/login?registered";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'inscription : La base de données a échoué. (" + e.getMessage() + ")");
            // Repasser les données saisies au cas où
            model.addAttribute("username", username);
            model.addAttribute("nom", nom);
            return "register";
        }
    }

}