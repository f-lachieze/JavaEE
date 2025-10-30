package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.example.ProjetJavaEE.Ex.modele.TypeSalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// L'import java.sql.Types n'est pas nécessaire ici et a été retiré.

public interface SalleRepository extends JpaRepository<Salle, String> {

    // 2. retourner toutes les salles de TD situées dans le bâtiment 36
    // Correction: typeS (et non types)
    List<Salle> findByTypeSEqualsAndBatiment_CodeB(TypeSalle typeS, String codeB);

    // 2. Retourner toutes les salles de TD situées dans le bâtiment 36 (JPQL)
    // Correction: s.typeS (et non s.types)
    @Query("SELECT s FROM Salle s WHERE s.typeS = 'TD' AND s.batiment.codeB = 'b36'")
    List<Salle> findTDSallesInB36();

    // 3. retourner les salles qui sont localisées dans un bâtiment dont le code est passé en paramètre
    List<Salle> findByBatiment_CodeB(String codeB);

    // 3. Retourner les salles qui sont localisées dans un bâtiment dont le code est passé en paramètre
    List<Salle> findByBatimentCodeB(String codeB);


    // 4. retourner toutes les salles d'un campus dont le nom est à préciser en paramètre
    @Query("SELECT s FROM Salle s JOIN s.batiment b JOIN b.campus c WHERE c.nomC = :nomCampus")
    List<Salle> findByCampusNom(@Param("nomCampus") String nomCampus);

    // 4. Retourner toutes les salles d'un campus dont le nom est à préciser en paramètre
    @Query("SELECT s FROM Salle s JOIN s.batiment b JOIN b.campus c WHERE c.nomC = :nomCampus")
    List<Salle> findSallesByCampusName(@Param("nomCampus") String nomCampus);

    // 5. retourner le nombre de salles par bâtiment
    @Query("SELECT b.codeB, COUNT(s) FROM Salle s JOIN s.batiment b GROUP BY b.codeB")
    List<Object[]> countSallesByBatiment();

    // 6. retourner le nombre de salles par type de salle
    @Query("SELECT s.typeS, COUNT(s) FROM Salle s GROUP BY s.typeS")
    List<Object[]> countSallesByType();


    // TP2 - Question 1: Compter le nombre de salles par Campus
    @Query("SELECT b.campus.nomC, COUNT(s) FROM Salle s JOIN s.batiment b GROUP BY b.campus.nomC")
    List<Object[]> countSallesByCampus();

    // TP2 - Question 2: Salles de TD de < 40 places, accessibles, à Montpellier
    @Query("SELECT s FROM Salle s JOIN s.batiment b JOIN b.campus c " +
            "WHERE c.ville = 'Montpellier' " +
            "AND s.typeS = 'TD' " +
            "AND s.capacite < 40 " +
            "AND s.accessibilite = 'oui'")
    List<Salle> findAccessibleTDSallesUnder40InMontpellier();

    // (TP2 - Question 3: Amphis par campus)
    @Query("SELECT s FROM Salle s JOIN s.batiment b JOIN b.campus c " +
            "WHERE c.nomC = :nomCampus AND s.typeS = 'amphi' AND s.capacite >= :capaciteMin")
    List<Salle> findAmphisByCampusAndCapaciteMin(@Param("nomCampus") String nomCampus, @Param("capaciteMin") int capaciteMin);


    // TP2 - Question 4: Calcule la capacité totale d'un bâtiment
    @Query("SELECT SUM(s.capacite) FROM Salle s WHERE s.batiment.codeB = :codeB")
    Long sumCapaciteByBatiment(@Param("codeB") String codeB);

    // TP2 - Question 4: Calcule la capacité totale d'un campus
    @Query("SELECT SUM(s.capacite) FROM Salle s JOIN s.batiment b JOIN b.campus c WHERE c.nomC = :nomCampus")
    Long sumCapaciteByCampus(@Param("nomCampus") String nomCampus);


    /**
     * (TP2 - Q5) Calcule le nombre total de groupes d'une taille donnée
     * pouvant être accueillis dans un bâtiment spécifique.
     */
    @Query("SELECT SUM(s.capacite / :tailleGroupe) FROM Salle s WHERE s.batiment.codeB = :codeB")
    Long sumGroupsByBatiment(@Param("codeB") String codeB, @Param("tailleGroupe") int tailleGroupe);


    /**
     * (TP2 - Q5) Calcule le nombre total de groupes d'une taille donnée
     * pouvant être accueillis sur un campus spécifique.
     */
    @Query("SELECT SUM(s.capacite / :tailleGroupe) FROM Salle s JOIN s.batiment b WHERE b.campus.nomC = :nomCampus")
    Long sumGroupsByCampus(@Param("nomCampus") String nomCampus, @Param("tailleGroupe") int tailleGroupe);


    /**
     * (TP2 - Q6) Calcule le nombre de groupes pour une liste de types de salles
     * dans un bâtiment donné.
     */
    @Query("SELECT SUM(s.capacite / :tailleGroupe) FROM Salle s WHERE s.batiment.codeB = :codeB AND s.typeS IN :types")
    Long sumGroupsByBatimentAndTypes(@Param("codeB") String codeB, @Param("tailleGroupe") int tailleGroupe, @Param("types") List<TypeSalle> types);

    /**
     * (TP2 - Q6) Calcule le nombre de groupes pour une liste de types de salles
     * sur un campus donné.
     */
    @Query("SELECT SUM(s.capacite / :tailleGroupe) FROM Salle s JOIN s.batiment b WHERE b.campus.nomC = :nomCampus AND s.typeS IN :types")
    Long sumGroupsByCampusAndTypes(@Param("nomCampus") String nomCampus, @Param("tailleGroupe") int tailleGroupe, @Param("types") List<TypeSalle> types);

}