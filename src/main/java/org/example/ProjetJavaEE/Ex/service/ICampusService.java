package org.example.ProjetJavaEE.Ex.service;

import java.util.List;
import java.util.Optional;

import org.example.ProjetJavaEE.Ex.modele.Salle;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.example.ProjetJavaEE.Ex.modele.Campus;


// ICampusService.java (CORRIGÉ)
public interface ICampusService {
	Campus createCampus(Campus campus);
	void deleteCampus(String id);
	Optional<Campus> campusParId(String nomC);
	void campusParVille(String ville);
	void campusEtBatiments();
	// On a retiré les 3 méthodes de gestion ici !
}