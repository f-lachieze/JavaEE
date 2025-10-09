package org.example.ProjetJavaEE.Ex.service.impl;

import org.example.ProjetJavaEE.Ex.domain.BatimentRepository;
import org.example.ProjetJavaEE.Ex.domain.SalleRepository;
import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GestionCampusServiceImpl implements GestionCampusService {

    // Injection des Repositories nécessaires pour accéder à la BD
    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    // ----------------------------------------------------------------------
    // 1. Implémentation du comptage des bâtiments et salles par campus
    // ----------------------------------------------------------------------

    // 1. Compter les bâtiments par campus
    @Override
    public List<Object[]> compterBatimentsParCampus() {
        // Cette méthode doit être définie dans BatimentRepository ou SalleRepository (si la jointure est possible)
        // Supposons qu'elle existe dans BatimentRepository
        return batimentRepository.countBatimentsByCampus();
    }

    // 1. Compter les salles par campus (méthode existante dans SalleRepository)
    @Override
    public List<Object[]> compterSallesParCampus() {
        return salleRepository.countSallesByCampus();
    }

    // ----------------------------------------------------------------------
    // 2. Implémentation de la recherche des salles de TD spécifiques à Montpellier
    // ----------------------------------------------------------------------

    /**
     * Correction: Compte le nombre de salles de TD < 40 places accessibles à Montpellier.
     * REMARQUE: Nécessite que le prototype dans l'interface soit Long (ou que la méthode dans
     * le Repository utilise un 'COUNT' et retourne un Long).
     */
    @Override
    public Long compterTDSallesMontpellier() {
        // Solution la plus propre: Ajouter une méthode 'countAccessibleTDSallesUnder40InMontpellier()'
        // dans SalleRepository qui utilise un COUNT JPQL.
        // Si vous utilisez la méthode existante qui retourne une List<Salle>, on compte sa taille.
        List<Salle> salles = salleRepository.findAccessibleTDSallesUnder40InMontpellier();
        return (long) salles.size();
    }


    // ----------------------------------------------------------------------
    // 3. Implémentation du listage des Amphis par campus et capacité (Question 3 du TP2)
    // ----------------------------------------------------------------------

    @Override
    public List<Salle> findAmphisByCampusAndCapaciteMin(String nomCampus, int capaciteMin) {
        return salleRepository.findAmphisByCampusAndCapaciteMin(nomCampus, capaciteMin);
    }


    // ----------------------------------------------------------------------
    // 4. Implémentation du calcul de la capacité totale
    // ----------------------------------------------------------------------

    @Override
    public Long calculerCapaciteTotaleBatiment(String codeB) {
        Long capacite = salleRepository.sumCapaciteByBatiment(codeB);
        // SUM() retourne NULL si aucun résultat, on retourne 0L dans ce cas.
        return capacite != null ? capacite : 0L;
    }

    @Override
    public Long calculerCapaciteTotaleCampus(String nomCampus) {
        Long capacite = salleRepository.sumCapaciteByCampus(nomCampus);
        return capacite != null ? capacite : 0L;
    }

    // ----------------------------------------------------------------------
    // 5. et 6. Implémentation des méthodes du TP1 (si elles sont dans l'interface)
    // ----------------------------------------------------------------------

    @Override
    public List<Object[]> compterSallesParBatiment() {
        return salleRepository.countSallesByBatiment();
    }

    @Override
    public List<Object[]> compterSallesByType() {
        return salleRepository.countSallesByType();
    }
}