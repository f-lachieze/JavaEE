package org.example.ProjetJavaEE.Ex.domain;

import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.modele.Composante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ComposanteRepository extends JpaRepository<Composante, String> {

    /**
     * (TP2 - Ex 2) Récupère l'ensemble des bâtiments (Set<Batiment>)
     * directement liés à une composante par son acronyme.
     * * La requête navigue à travers la relation 'exploite' de l'entité Composante.
     */
    @Query("SELECT c.exploite FROM Composante c WHERE c.acronyme = :acronyme")
    Set<Batiment> findBatimentsExploitesByAcronyme(@Param("acronyme") String acronyme);

    /**
     * (TP2 - Ex 2) Compte les salles exploitées par une composante, regroupées par type.
     * La requête navigue de Composante -> Batiment -> Salle.
     */
    @Query("SELECT s.typeS, COUNT(s.numSalle) FROM Composante c JOIN c.exploite b JOIN b.salles s WHERE c.acronyme = :acronyme GROUP BY s.typeS")
    List<Object[]> countSallesExploiteesByType(@Param("acronyme") String acronyme);


}