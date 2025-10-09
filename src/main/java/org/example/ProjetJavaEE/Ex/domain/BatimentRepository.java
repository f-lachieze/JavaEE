package org.example.ProjetJavaEE.Ex.domain;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.example.ProjetJavaEE.Ex.modele.Batiment;

import org.example.ProjetJavaEE.Ex.modele.*;

@Repository
public interface BatimentRepository extends JpaRepository<Batiment, String> {
	// JPQL Query
	@Query("SELECT b FROM Batiment b WHERE b.codeB IN :ids")
	List<Batiment> findByIds(@Param("ids") List<String> ids);
	
	List<Batiment> findByCampus(Campus campus);


	// TP2 - Question 1: Compter le nombre de bâtiments par Campus
	@Query("SELECT b.campus.nomC, COUNT(b) FROM Batiment b GROUP BY b.campus.nomC")
	List<Object[]> countBatimentsByCampus();

	// ... Autres méthodes pour le TP2 ...

}