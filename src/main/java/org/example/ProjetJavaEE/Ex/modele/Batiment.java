package org.example.ProjetJavaEE.Ex.modele;

import java.util.Set;
import jakarta.persistence.*;

@Entity
@Table(name = "BATIMENT")
public class Batiment {

	// Clé Primaire (déjà présente) [cite: 16, 38]
	@Id
	@Column(name = "codeB", unique = true, nullable = false)
	private String codeB;
	
	@Column(name= "anneC", nullable = false)
	private int anneeC;

	// Relation ManyToOne avec Campus (pour l'enrichir)
	// Campus(nomC) est la FK dans la table Batiment [cite: 42]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campus")
	private Campus campus; // Un bâtiment appartient à un campus [cite: 5, 11]

	// 1. Association One-to-Many avec Salle
	// MappedBy pointe vers l'attribut 'building' dans la classe Salle [cite: 17]
	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Salle> salles;

	// 2. Association Many-to-Many avec Composante (Inverse de celle dans Composante)
	// MappedBy pointe vers l'attribut 'exploite' dans la classe Composante
	@ManyToMany(mappedBy = "exploite")
	private Set<Composante> teams; // Nom 'team' tiré de l'UML [cite: 14]

	// Constructeur par défaut (requis)
	public Batiment() {} 
	
	public Batiment(String codeB, int anneeC, Campus campus) {
		super();
		this.codeB = codeB;
		this.anneeC = anneeC;
		this.campus = campus;
	}

	public String getCodeB() {
		return codeB;
	}

	public void setCodeB(String codeB) {
		this.codeB = codeB;
	}

	public int getAnneeC() {
		return anneeC;
	}

	public void setAnneeC(int anneeC) {
		this.anneeC = anneeC;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}
	
	@Override
	public String toString() {
		return "Batiment [codeB=" + codeB  + ", anneeC=" + anneeC + "]";
	}
	
}
