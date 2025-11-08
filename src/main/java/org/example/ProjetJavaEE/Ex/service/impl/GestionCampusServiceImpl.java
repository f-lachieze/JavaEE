package org.example.ProjetJavaEE.Ex.service.impl;

import org.example.ProjetJavaEE.Ex.domain.BatimentRepository;
import org.example.ProjetJavaEE.Ex.domain.CampusRepository;
import org.example.ProjetJavaEE.Ex.domain.SalleRepository;
import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import org.example.ProjetJavaEE.Ex.modele.TypeSalle;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GestionCampusServiceImpl implements GestionCampusService {

    // Injection des Repositories nécessaires pour accéder à la BD
    @Autowired
    private SalleRepository salleRepository;

    @Autowired
    private BatimentRepository batimentRepository;

    // Injection du Repository pour l'accès aux données (DAO)
    @Autowired
    private CampusRepository campusRepository;

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

    // TP2 question 3
    @Override
    public List<Salle> listerAmphisParCampusAvecCapaciteMin(String nomCampus, int capaciteMin) {
        // On appelle la méthode du repository en spécifiant que le type est AMPHI
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
    @Transactional(readOnly = true)
    public Long calculerCapaciteTotaleCampus(String nomCampus) {

        Long capacite = salleRepository.calculerCapaciteTotaleParCampus(nomCampus);

        // Retourne la capacité trouvée, ou 0 si le résultat était null (pas de salle/bâtiment).
        return capacite != null ? capacite : 0L;
    }

    // TP2 question 5
    @Override
    public Long calculerNombreGroupesParBatiment(String codeB, int tailleGroupe) {
        Long totalGroupes = salleRepository.sumGroupsByBatiment(codeB, tailleGroupe);
        // Si la requête ne trouve aucune salle, SUM retourne NULL. On retourne 0 dans ce cas.
        return totalGroupes != null ? totalGroupes : 0L;
    }

    @Override
    public Long calculerNombreGroupesParCampus(String nomCampus, int tailleGroupe) {
        Long totalGroupes = salleRepository.sumGroupsByCampus(nomCampus, tailleGroupe);
        return totalGroupes != null ? totalGroupes : 0L;
    }

    @Override
    public Long calculerNbGroupesAmphiOuTdParBatiment(String codeB, int tailleGroupe) {
        List<TypeSalle> typesRecherches = List.of(TypeSalle.AMPHI, TypeSalle.TD);
        Long totalGroupes = salleRepository.sumGroupsByBatimentAndTypes(codeB, tailleGroupe, typesRecherches);
        return totalGroupes != null ? totalGroupes : 0L;
    }

    @Override
    public Long calculerNbGroupesAmphiOuTdParCampus(String nomCampus, int tailleGroupe) {
        List<TypeSalle> typesRecherches = List.of(TypeSalle.AMPHI, TypeSalle.TD);
        Long totalGroupes = salleRepository.sumGroupsByCampusAndTypes(nomCampus, tailleGroupe, typesRecherches);
        return totalGroupes != null ? totalGroupes : 0L;
    }


    // ----------------------------------------------------------------------
    // 5. et 6. Implémentation des méthodes du TP1
    // ----------------------------------------------------------------------

    @Override
    public List<Object[]> compterSallesParBatiment() {
        return salleRepository.countSallesByBatiment();
    }

    @Override
    public List<Object[]> compterSallesByType() {
        return salleRepository.countSallesByType();
    }



    // thymeleaf

    @Override
    public List<Campus> findAllCampus() {
        // L'implémentation utilise la méthode findAll() héritée de JpaRepository
        return campusRepository.findAll();
    }
    @Override
    @Transactional(readOnly = true) // Lecture seule pour optimiser
    public Campus findCampusWithBatiments(String nomCampus) {
        // Utilisation de la méthode JPQL pour charger le campus et ses bâtiments en une seule requête
        return campusRepository.findByNomCFetchBatiments(nomCampus)
                .orElseThrow(() -> new IllegalArgumentException("Campus non trouvé : " + nomCampus));
    }

    @Override
    @Transactional(readOnly = true)
    public Batiment findBatimentWithSalles(String codeBatiment) {
        // Utilise la requête JPQL ci-dessus pour charger le bâtiment et toutes ses salles
        return batimentRepository.findByCodeBFetchSalles(codeBatiment)
                .orElseThrow(() -> new IllegalArgumentException("Bâtiment non trouvé : " + codeBatiment));
    }

    @Override
    @Transactional // Nécessaire pour les opérations d'écriture (INSERT)
    public Salle saveSalle(Salle salle) {
        // La méthode save() de JpaRepository gère l'insertion si l'ID n'existe pas.
        return salleRepository.saveAndFlush(salle);
    }

    @Override
    @Transactional(readOnly = true)
    public Salle findSalleByNumSalle(String numSalle) {
        // findById renvoie un Optional. get() force la récupération (lance NoSuchElementException si non trouvé)
        return salleRepository.findById(numSalle)
                .orElseThrow(() -> new IllegalArgumentException("Salle non trouvée : " + numSalle));
    }

    @Override
    @Transactional
    public void deleteSalle(String numSalle) {
        // La méthode deleteById() du Repository est utilisée
        salleRepository.deleteById(numSalle);
    }


}