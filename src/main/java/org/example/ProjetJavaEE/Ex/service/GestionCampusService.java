package org.example.ProjetJavaEE.Ex.service;

import org.example.ProjetJavaEE.Ex.modele.Salle;

import java.util.List;

public interface GestionCampusService {

    // 1. De combien de bâtiments et de salles d'enseignement dispose chaque campus?
    List<Object[]> compterBatimentsParCampus();
    List<Object[]> compterSallesParCampus();

    // 2. Combien de salles de TD de moins de 40 places et accessibles aux personnes à mobilité réduite
    // sont disponibles sur les campus situés à Montpellier?
    Long compterTDSallesMontpellier();

    // 4. Quelle est la capacité totale en terme de places assises d'un campus en particulier ou d'un de
    // ses bâtiments?
    Long calculerCapaciteTotaleCampus(String nomCampus);
    Long calculerCapaciteTotaleBatiment(String codeB);


    List<Salle> findAmphisByCampusAndCapaciteMin(String nomCampus, int capaciteMin);
    List<Object[]> compterSallesParBatiment();
    List<Object[]> compterSallesByType();

    // ... Ajoutez les autres méthodes du TP2 (Questions 3, 5, 6) ...
}