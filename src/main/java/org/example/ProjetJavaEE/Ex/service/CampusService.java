package org.example.ProjetJavaEE.Ex.service;

import java.util.Optional;

import org.example.ProjetJavaEE.Ex.modele.Campus;


// ICampusService.java
public interface CampusService {
	Campus createCampus(Campus campus);
	void deleteCampus(String id);
	Optional<Campus> campusParId(String nomC);
	void campusParVille(String ville);
	void campusEtBatiments();

    // Dans GestionCampusService.java (Interface)


    /** Calcule la capacité totale en places assises d'un bâtiment donné.  */
    Long calculerCapaciteTotaleBatiment(String codeBatiment);


}