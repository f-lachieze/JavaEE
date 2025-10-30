package org.example.ProjetJavaEE.Ex.service.impl;

import org.example.ProjetJavaEE.Ex.domain.ComposanteRepository;
import org.example.ProjetJavaEE.Ex.modele.Batiment;
import org.example.ProjetJavaEE.Ex.service.IGestionComposanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service // Ne pas oublier de déclarer la classe comme un Service Spring
public class GestionComposanteServiceImpl implements IGestionComposanteService {

    @Autowired
    private ComposanteRepository composanteRepository;

    @Override
    public Set<Batiment> listerBatimentsExploites(String acronymeComposante) {
        // Nous allons créer cette méthode dans le repository à l'étape suivante
        return composanteRepository.findBatimentsExploitesByAcronyme(acronymeComposante);
    }

    @Override
    public List<Object[]> compterSallesExploiteesParType(String acronymeComposante) {
        return composanteRepository.countSallesExploiteesByType(acronymeComposante);
    }
}