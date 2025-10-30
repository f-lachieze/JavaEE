package org.example.ProjetJavaEE.Ex.service;

import org.example.ProjetJavaEE.Ex.modele.Batiment;
import java.util.Set;
import java.util.List;

/**
 * Interface pour les services métier liés à la gestion
 * des capacités d'une Composante (Exercice 2).
 */
public interface IGestionComposanteService {

    /**
     * Cas d'utilisation : Lister les bâtiments exploités par une composante.
     * @param acronymeComposante L'acronyme de la composante (ex: "FDS")
     * @return Un Set de Bâtiments
     */
    Set<Batiment> listerBatimentsExploites(String acronymeComposante);


    /**
     * Cas d'utilisation : Compter les salles exploitées par une composante, par type.
     * @param acronymeComposante L'acronyme de la composante
     * @return Une liste de [TypeSalle, Long]
     */
    List<Object[]> compterSallesExploiteesParType(String acronymeComposante);


}