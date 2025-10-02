package org.example.ProjetJavaEE.Ex.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.example.ProjetJavaEE.Ex.modele.Campus;


@Repository
public interface CampusRepository extends JpaRepository<Campus, String> {
	
	List<Campus> findByVille(String ville);
	
}
