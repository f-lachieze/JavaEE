package org.example.ProjetJavaEE.Ex.modele;

import java.util.Set;
import jakarta.persistence.*;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.HashSet; // <-- Assurez-vous que cet import est présent

@Entity
@Table(name = "BATIMENT")
public class Batiment {

	// Clé Primaire (déjà présente)
	@Id
	@Column(name = "codeB", unique = true, nullable = false)
	private String codeB;
	
	@Column(name= "anneec", nullable = false)
	private int anneeConstruction;

	// Relation ManyToOne avec Campus (pour l'enrichir)
	// Campus(nomC) est la FK dans la table Batiment [cite: 42]
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "campus", nullable = false)
	private Campus campus; // Un bâtiment appartient à un campus [cite: 5, 11]

	// 1. Association One-to-Many avec Salle
	// MappedBy pointe vers l'attribut 'building' dans la classe Salle [cite: 17]
	@OneToMany(mappedBy = "batiment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Salle> salles = new HashSet<>();

    // 2. Association One-to-Many avec Exploite
// 'mappedBy' pointe vers l'attribut 'batiment' dans la classe Exploite
    @OneToMany(mappedBy = "batiment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Exploite> exploitations = new HashSet<>();

	// Constructeur par défaut (requis)
	public Batiment() {} 
	
	public Batiment(String codeB, int anneeConstruction, Campus campus) {
		super();
		this.codeB = codeB;
		this.anneeConstruction = anneeConstruction;
		this.campus = campus;
	}

    // ... (vos champs codeB, anneeConstruction, campus)



// --- FIN DE L'AJOUT ---


	public String getCodeB() {
		return codeB;
	}

	public void setCodeB(String codeB) {
		this.codeB = codeB;
	}

	public int getAnneeConstruction() {
		return anneeConstruction;
	}

	public void setAnneeConstruction(int anneeConstruction) {
		this.anneeConstruction = anneeConstruction;
	}

	public Campus getCampus() {
		return campus;
	}

	public void setCampus(Campus campus) {
		this.campus = campus;
	}
	
	@Override
	public String toString() {
		return "Batiment [codeB=" + codeB  + ", anneeC=" + anneeConstruction + "]";
	}

    public Set<Salle> getSalles() {
        return salles;
    }

    public void setSalles(Set<Salle> salles) {
        this.salles = salles;
    }

    // --- NOUVEAU GETTER/SETTER pour la collection de liaison ---

    public Set<Exploite> getExploitations() {
        return exploitations;
    }

    public void setExploitations(Set<Exploite> exploitations) {
        this.exploitations = exploitations;
    }
	
}
