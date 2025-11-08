package org.example.ProjetJavaEE.Ex.service.impl;
import org.example.ProjetJavaEE.Ex.domain.SalleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.example.ProjetJavaEE.Ex.domain.CampusRepository;
import org.example.ProjetJavaEE.Ex.modele.Campus;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CampusServiceImpl implements org.example.ProjetJavaEE.Ex.service.CampusService {

	private final CampusRepository cr;

    @Autowired
    public CampusServiceImpl(CampusRepository cr) {
        this.cr = cr;
    }

    @Autowired
    private SalleRepository salleRepository;

    public List<Campus> findAll() {
        return cr.findAll();
    }
    
    public Iterable<String[]> showsEntities() {
        return cr.campusEtCodeB();
    }
    
    public void campusEtBatiments()
	{  
	Iterable<String[]> e = showsEntities();
	System.out.println("les campus avec leurs batiments (codes) :");
	if (StreamSupport.stream(e.spliterator(), false).count() != 0) { 
   	   for (String[] s : e)
          {
          for (String entry : s) {
        	    System.out.print(entry+" ");
        	}
          System.out.println("");
          }
      } else System.out.println("pas grand chose dans la base");
	} 
    
    
    public void campusParVille(String ville)
	{  Iterable<Campus> e = cr.findByVille(ville);
	  System.out.println("La liste des campus de "+ville);
      if (StreamSupport.stream(e.spliterator(), false).count() != 0) { 
   	   for (Campus c : e)
          { System.out.println(c.toString());}
      } else System.out.println("pas de campus connu sur "+ville);
	} 
       
    public Optional<Campus> campusParId(String id) {
        return cr.findById(id);
    }
    @Transactional
    public Campus createCampus(Campus campus) {
        return cr.save(campus);
    }
    @Transactional
    public Campus updateCampus(String id, Campus updatedCampus) {
    	return cr.findById(id)
                .map(campus -> {
                    campus.setVille(updatedCampus.getVille());
                    campus.setBatiments(updatedCampus.getBatiments());
                    return cr.save(campus);
                })
                .orElseThrow(() -> new RuntimeException("Campus not found with nomC: " + id));
    }
    @Transactional
    public void deleteCampus(String id) {
        cr.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Long calculerCapaciteTotaleBatiment(String codeBatiment) {

        // Le Repository renvoie la somme (Long), ou null si aucune salle n'est trouvée.
        Long capacite = salleRepository.calculerCapaciteTotaleParBatiment(codeBatiment);

        // Retourne la capacité trouvée, ou 0 si le résultat était null (pas de salle).
        return capacite != null ? capacite : 0L;
    }
    
}