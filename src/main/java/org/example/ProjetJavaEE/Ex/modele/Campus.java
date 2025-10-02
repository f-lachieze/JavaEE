package org.example.ProjetJavaEE.Ex.modele;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
public class Campus {
	
	@Id
	private String nomC;
	
	private String ville;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy="campus", cascade = CascadeType.REMOVE)
	private List<Batiment> batiments = new ArrayList<Batiment>();
	
	public Campus() {
	}
	
	public Campus(String nomC, String ville) {
		super();
		this.nomC = nomC;
		this.ville = ville;
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

	@Override
	public String toString() {
		return "Campus [nomC=" + nomC + ", ville=" + ville + "]";
	}
	
}
