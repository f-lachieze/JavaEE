package org.example.ProjetJavaEE.Ex.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.example.ProjetJavaEE.Ex.modele.Campus;


@Repository
public interface CampusRepository extends JpaRepository<Campus, String> {
	
	List<Campus> findByVille(String ville);

	// CORRECTION : AJOUT DE L'ANNOTATION @Query
	// Cette requête joint Campus et Batiment pour retourner le nom du campus et le code du bâtiment.
	@Query("SELECT c.nomC, b.codeB FROM Campus c JOIN Batiment b ON b.campus = c")
	Iterable<String[]> campusEtCodeB();

}
