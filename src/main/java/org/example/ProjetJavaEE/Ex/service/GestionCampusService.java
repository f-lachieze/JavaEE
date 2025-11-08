package org.example.ProjetJavaEE.Ex.service;

import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.example.ProjetJavaEE.Ex.modele.Salle;

import java.util.List;

public interface GestionCampusService {

    // 1. De combien de bâtiments et de salles d'enseignement dispose chaque campus?
    List<Object[]> compterBatimentsParCampus();
    List<Object[]> compterSallesParCampus();

    // 2. Combien de salles de TD de moins de 40 places et accessibles aux personnes à mobilité réduite
    // sont disponibles sur les campus situés à Montpellier?
    Long compterTDSallesMontpellier();

    // 3. Lister les amphis d'un campus avec une capacité minimale
    List<Salle> listerAmphisParCampusAvecCapaciteMin(String nomCampus, int capaciteMin);

    // 4. Quelle est la capacité totale en terme de places assises d'un campus en particulier ou d'un de
    // ses bâtiments?
    Long calculerCapaciteTotaleCampus(String nomCampus);
    Long calculerCapaciteTotaleBatiment(String codeB);


    List<Salle> findAmphisByCampusAndCapaciteMin(String nomCampus, int capaciteMin);
    List<Object[]> compterSallesParBatiment();
    List<Object[]> compterSallesByType();


    // 5. Calculer le nombre de groupes dans un bâtiment
    Long calculerNombreGroupesParBatiment(String codeB, int tailleGroupe);

    // 5. Calculer le nombre de groupes dans un campus
    Long calculerNombreGroupesParCampus(String nomCampus, int tailleGroupe);

    // 6. Calculer le nombre de groupes (Amphi ou TD) dans un bâtiment
    Long calculerNbGroupesAmphiOuTdParBatiment(String codeB, int tailleGroupe);

    // 6. Calculer le nombre de groupes (Amphi ou TD) sur un campus
    Long calculerNbGroupesAmphiOuTdParCampus(String nomCampus, int tailleGroupe);

    // Déclaration de la méthode pour obtenir tous les campus
    List<Campus> findAllCampus();

    /** Retourne un campus par son nom, chargé avec ses bâtiments */
    Campus findCampusWithBatiments(String nomCampus);

}