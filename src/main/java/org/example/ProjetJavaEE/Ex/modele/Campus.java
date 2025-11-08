package org.example.ProjetJavaEE.Ex.modele;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Campus {
	
	@Id // la clé primaire de l'entité
	private String nomC;
	
	private String ville;

    // un campus peut contenir plusieurs batiments
	@OneToMany(fetch = FetchType.LAZY, mappedBy="campus", cascade = CascadeType.REMOVE)
	private List<Batiment> batiments = new ArrayList<Batiment>();



    // Relation N-1 : Plusieurs campus appartiennent à une université
    // C'est l'entité "propriétaire" de la relation
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universite_acronyme", nullable = false) // 'nullable = false' signifie qu'un campus DOIT avoir une université
    private Universite universite;


	public Campus() {
	}

    public Campus(String nomC, String ville, Universite universite) {
        this.nomC = nomC;
        this.ville = ville;
        this.universite = universite; // Initialisation du champ obligatoire
        this.batiments = new ArrayList<Batiment>();
    }
	
	public String getNomC() {
		return nomC;
	}
	public void setNomC(String nomC) {
		this.nomC = nomC;
	}
	public String getVille() {
		return ville;
	}
	public void setVille(String ville) {
		this.ville = ville;
	}

	public List<Batiment> getBatiments() {
		return batiments;
	}

	public void setBatiments(List<Batiment> batiments) {
		this.batiments = batiments;
	}

    public Universite getUniversite() {
        return universite;
    }

    public void setUniversite(Universite universite) {
        this.universite = universite;
    }

	@Override
	public String toString() {
		return "Campus [nomC=" + nomC + ", ville=" + ville + "]";
	}
	
}
