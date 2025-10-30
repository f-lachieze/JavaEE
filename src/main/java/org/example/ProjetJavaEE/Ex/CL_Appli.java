package org.example.ProjetJavaEE.Ex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.example.ProjetJavaEE.Ex.service.GestionCampusService;
import org.example.ProjetJavaEE.Ex.service.IGestionComposanteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.transaction.Transactional;
import org.example.ProjetJavaEE.Ex.domain.* ;
import org.example.ProjetJavaEE.Ex.modele.* ;

@Transactional 
@SpringBootApplication
public class CL_Appli implements CommandLineRunner {
		 @Autowired
		 private BatimentRepository batimentRepository;
		 @Autowired
		 private CampusRepository campusRepository;

		 @Autowired
		 private SalleRepository salleRepository;


		 @Autowired
		 private ComposanteRepository composanteRepository;

    @Autowired
    private IGestionComposanteService gestionComposanteService;

	// ... Injections des Repositories ...

	// NOUVEAU : Injection du Service
	// le bean de l'interface Gesttion campus service
	@Autowired
	private GestionCampusService gcs; // gcs pour GestionCampusService

    @Autowired
    private UniversiteRepository universiteRepository;



	public static void main(String[] args) {
			SpringApplication.run(CL_Appli.class, args);
		}
		
	public void run(String... args) throws Exception {

        System.out.println("\n=======================================================");
        System.out.println("--- Démarrage de la Création des Données (Exercices 2 & 3) ---");
        System.out.println("=======================================================");

        // --- 1. Création des Universités ---

        // Création de l'Université de Montpellier (UM)
        Universite um = universiteRepository.findById("UM").orElseGet(() -> {
            Universite newUm = new Universite();
            newUm.setAcronyme("UM");
            newUm.setNom("Université de Montpellier");
            newUm.setCreation(2015);
            newUm.setPresidence("Philippe Augé");
            System.out.println("-> CRÉATION Université : UM");
            return universiteRepository.saveAndFlush(newUm);
        });

        // Création de l'Université Paul Valéry (UPVD)
        Universite upvd = universiteRepository.findById("UPVD").orElseGet(() -> {
            Universite newUpvd = new Universite();
            newUpvd.setAcronyme("UPVD");
            newUpvd.setNom("Université Paul Valéry");
            System.out.println("-> CRÉATION Université : UPVD");
            return universiteRepository.saveAndFlush(newUpvd);
        });

        // --- 2. Création des Campus et Bâtiments ---

        // Création du Campus FDE Nimes (lié à l'UM)
        Campus fdeNimes = campusRepository.findById("FDE Nimes").orElseGet(() -> {
            Campus newCampus = new Campus();
            newCampus.setNomC("FDE Nimes");
            newCampus.setVille("Nîmes");
            newCampus.setUniversite(um); // <-- LIAISON OBLIGATOIRE
            System.out.println("-> CRÉATION Campus : FDE Nimes (lié à UM)");
            return campusRepository.saveAndFlush(newCampus);
        });

        // Création du Bâtiment "bt1 nimes" (lié à FDE Nimes)
        Batiment bt1Nimes = batimentRepository.findById("bt1 nimes").orElseGet(() -> {
            Batiment newBatiment = new Batiment();
            newBatiment.setCodeB("bt1 nimes");
            newBatiment.setAnneeConstruction(2024);
            newBatiment.setCampus(fdeNimes); // Liaison au campus
            System.out.println("-> CRÉATION Bâtiment : bt1 nimes (lié à FDE Nimes)");
            return batimentRepository.saveAndFlush(newBatiment);
        });

        // Création du Campus Paul Valéry (lié à UPVD)
        Campus campusPaulValery = campusRepository.findById("Paul Valéry Campus").orElseGet(() -> {
            Campus newCampus = new Campus();
            newCampus.setNomC("Paul Valéry Campus");
            newCampus.setVille("Montpellier");
            newCampus.setUniversite(upvd); // <-- LIAISON OBLIGATOIRE
            System.out.println("-> CRÉATION Campus : Paul Valéry Campus (lié à UPVD)");
            return campusRepository.saveAndFlush(newCampus);
        });

        // Création du Bâtiment "Elearning Center" (lié à Paul Valéry)
        batimentRepository.findById("Elearning Center").orElseGet(() -> {
            Batiment newBatiment = new Batiment();
            newBatiment.setCodeB("Elearning Center");
            newBatiment.setAnneeConstruction(2020);
            newBatiment.setCampus(campusPaulValery); // Liaison au campus
            System.out.println("-> CRÉATION Bâtiment : Elearning Center (lié à Paul Valéry Campus)");
            return batimentRepository.saveAndFlush(newBatiment);
        });


        // --- 3. Création des Salles (liées à "bt1 nimes") ---

        // Nous avons besoin de la vraie instance de bt1Nimes pour la mettre à jour
        Batiment batimentPourSalles = batimentRepository.findById("bt1 nimes").get();

        salleRepository.findById("A01").orElseGet(() -> {
            Salle newSalle = new Salle();
            newSalle.setNumSalle("A01");
            newSalle.setCapacite(200);
            newSalle.setTypeS(TypeSalle.AMPHI);

            // --- MISE À JOUR DES DEUX CÔTÉS ---
            newSalle.setBatiment(batimentPourSalles); // 1. Côté Salle
            batimentPourSalles.getSalles().add(newSalle); // 2. Côté Batiment
            // --- FIN DE LA MODIFICATION ---

            System.out.println("-> CRÉATION Salle : A01");

            return newSalle;
            // Pas besoin de sauvegarder le bâtiment, 'cascade' s'en charge.
        });

        salleRepository.findById("TP101").orElseGet(() -> {
            Salle newSalle = new Salle();
            newSalle.setNumSalle("TP101");
            newSalle.setCapacite(30);
            newSalle.setTypeS(TypeSalle.TP);

            // --- MISE À JOUR DES DEUX CÔTÉS ---
            newSalle.setBatiment(batimentPourSalles); // 1. Côté Salle
            batimentPourSalles.getSalles().add(newSalle); // 2. Côté Batiment
            // --- FIN DE LA MODIFICATION ---

            System.out.println("-> CRÉATION Salle : TP101");
            return newSalle;
        });

        // MAINTENANT on sauvegarde le parent.
        // La cascade va automatiquement sauvegarder A01 et TP101.
        batimentRepository.saveAndFlush(batimentPourSalles);

        // Il est plus sûr de sauvegarder le bâtiment après les ajouts
        // pour que la session Hibernate soit 100% à jour.
        batimentRepository.saveAndFlush(batimentPourSalles);

        // ... (fin du test du cas d'utilisation 1)

        // --- TEST DU CAS D'UTILISATION 2 ---
        System.out.println("\n-> 2. Comptage des salles exploitées par 'FDS' (par type) :");
        List<Object[]> comptageParType = gestionComposanteService.compterSallesExploiteesParType("FDS");

        if (comptageParType.isEmpty()) {
            System.out.println("Aucune salle exploitée trouvée pour cette composante.");
        } else {
            comptageParType.forEach(resultat ->
                    System.out.println("- Type: " + resultat[0] + " | Nombre: " + resultat[1])
            );
        }









        tsLesBatiments();
		campusParVille("Montpellier");
		List<String> codes = new ArrayList<String>();
		codes.add("triolet_b36");
		codes.add("triolet_b16");
		certainsBatiments(codes);
		testSallesSpeciales();


		System.out.println("\n--- Démarrage de l'Exercice 2 du TP1 (Méthodes Repository) ---");

		// 2. Tester les salles de TD dans le bâtiment 'bt1 nimes' (ou B36 si vous changez l'ID)
		tslesSallesB36();

		// 3. Tester les salles par code de bâtiment
		tslesSallesParBatiment("bt1 nimes"); // Utilisez le bâtiment que vous avez créé

		// 4. Tester le comptage par bâtiment
		tsComptageParBatiment();

		System.out.println("\n=======================================================");
		System.out.println("--- Démarrage de l'Exercice 1 du TP2 (Couche Service) ---");
		System.out.println("=======================================================");

		// 1. Tester le comptage par campus (Bâtiments et Salles)
		tsCompterBatimentsEtSallesParCampus();

		// 2. Tester les salles de TD spécifiques à Montpellier
		tsSallesTDSpecifiquesMontpellier();

		// 3.  tester si on a des amphis du campus triolet qui ont plus de 80 places
		tsListerAmphisSpecifiques();

		// TP2 Q4.
		tsCapaciteTotale();

		// TP2 Q5
		System.out.println("\n-> TP2, Q5. Nombre de groupes de 30 étudiants :");

		// Test pour un bâtiment
		String codeBatiment = "bt1 nimes";
		Long groupesBatiment = gcs.calculerNombreGroupesParBatiment(codeBatiment, 30);
		System.out.println("Bâtiment '" + codeBatiment + "' peut accueillir " + groupesBatiment + " groupes.");

		// Test pour un campus
		String nomCampus = "FDE Nimes";
		Long groupesCampus = gcs.calculerNombreGroupesParCampus(nomCampus, 30);
		System.out.println("Campus '" + nomCampus + "' peut accueillir " + groupesCampus + " groupes.");

		// TP2 Q6

		System.out.println("\n-> TP2 Q6. Nombre de groupes (Amphi ou TD) pour une taille de 25 :");

		// Test pour un bâtiment
		String codeBatimentQ6 = "bt1 nimes"; // <-- Changement de nom
		Long groupesBatimentQ6 = gcs.calculerNbGroupesAmphiOuTdParBatiment(codeBatimentQ6, 25); // <-- Changement de nom
		System.out.println("Bâtiment '" + codeBatimentQ6 + "' peut accueillir " + groupesBatimentQ6 + " groupes dans ses amphis/salles de cours.");

		// Test pour un campus
		String nomCampusQ6 = "FDE Nimes"; // <-- Changement de nom
		Long groupesCampusQ6 = gcs.calculerNbGroupesAmphiOuTdParCampus(nomCampusQ6, 25); // <-- Changement de nom
		System.out.println("Campus '" + nomCampusQ6 + "' peut accueillir " + groupesCampusQ6 + " groupes dans ses amphis/salles de cours.");

		// ... autres appels pour le TP2 ...

        // ... fin des tests de l'Exercice 1


	}







	
	
	public void tsLesBatiments()
	{  Iterable<Batiment> e = batimentRepository.findAll();
	  System.out.println("La liste des batiments");
      if (e != null) { 
   	   for (Batiment b : e)
          { System.out.println(b.getCodeB()+"\t "+b.getAnneeConstruction()+"\t "+b.getCampus().getNomC());}
      } else System.out.println("nada");
	} 
	
	public void campusParVille(String ville)
	{  Iterable<Campus> e = campusRepository.findByVille(ville);
	  System.out.println("La liste des campus de "+ville);
      if (e != null) { 
   	   for (Campus c : e)
          { System.out.println(c.toString());}
      } else System.out.println("nada");
	} 
	
	public void certainsBatiments(List<String> codes)
	{  Iterable<Batiment> e = batimentRepository.findByIds(codes);
	  System.out.println("La liste de certains batiments");
      if (e != null) { 
   	   for (Batiment b : e)
          { System.out.println(b.getCodeB()+"\t "+b.getAnneeConstruction()+"\t "+b.getCampus().getNomC());}
      } else System.out.println("nada");
	}

	// Exemple pour tester les points 2 et 3
	public void testSallesSpeciales() {
		System.out.println("\n--- 2 & 3. Salles du batiment B36 (TD uniquement) ---");
		// Supposons qu'il existe un bâtiment de code "b36"
		String codeB_test = "b36";

		// Point 2 : Salle de TD dans le bâtiment b36
		List<Salle> sallesTD_B36 = // Dans CL_Appli.java, mettez à jour la ligne d'appel
                salleRepository.findByTypeSEqualsAndBatiment_CodeB(TypeSalle.TD, "b36");
		System.out.println("Salles TD dans " + codeB_test + ": " + sallesTD_B36.size());
		for (Salle s : sallesTD_B36) {
			System.out.println("\t[TD] " + s.getNumSalle() + " - Capacité: " + s.getCapacite());
		}

		// Point 3 : Toutes les salles du bâtiment b36
		List<Salle> toutesSalles_B36 = salleRepository.findByBatiment_CodeB("b36");
		System.out.println("Toutes Salles dans " + codeB_test + ": " + toutesSalles_B36.size());
		// ...
		}

	// --- Exemple de méthode de test 2 ---
	public void tslesSallesB36() {
		System.out.println("\n-> 2. Salles de TD dans B36 (ou bt1 nimes si l'ID est mis à jour dans la requête):");
		// NOTE: Si vous avez gardé 'b36' dans le @Query, la liste sera vide.
		// Si vous avez modifié la @Query pour utiliser 'bt1 nimes', elle devrait afficher les salles TD.

		// Modifiez la requête findTDSallesInB36 dans SalleRepository pour utiliser 'bt1 nimes' si vous voulez voir les données insérées :
		// @Query("SELECT s FROM Salle s WHERE s.types = 'TD' AND s.batiment.codeB = 'bt1 nimes'")

		List<Salle> salles = salleRepository.findTDSallesInB36();
		if (salles.isEmpty()) {
			System.out.println("Aucune salle trouvée.");
		} else {
			salles.forEach(salle -> System.out.println(salle.getNumSalle()));
		}
	}

	// --- Exemple de méthode de test 3 ---
	public void tslesSallesParBatiment(String codeB) {
		System.out.println("\n-> 3. Salles du bâtiment " + codeB + ":");
		List<Salle> salles = salleRepository.findByBatimentCodeB(codeB);
		salles.forEach(salle -> System.out.println(salle.getNumSalle() + " - Capacité: " + salle.getCapacite()));
	}

	// --- Exemple de méthode de test 5 ---
	public void tsComptageParBatiment() {
		System.out.println("\n-> 5. Comptage des salles par bâtiment:");
		List<Object[]> resultats = salleRepository.countSallesByBatiment();
		for (Object[] resultat : resultats) {
			System.out.println("Bâtiment: " + resultat[0] + " | Nombre de salles: " + resultat[1]);
		}
	}


// sans transactional, save et saveAndFluch pourraient échouer
	@Transactional // Nécessaire pour les opérations de création/modification
	public void creationEtAssociationNimes() {
		System.out.println("\n--- Démarrage de l'Exercice 2 (Partie Création) ---");

		// --- 1. Créer Campus et Batiment associés ---

		// 1.1 Créer un nouveau Campus : 'FDE Nimes' à 'Nimes'
		Campus nimesCampus = new Campus();
		nimesCampus.setNomC("FDE Nimes"); // Clé primaire
		nimesCampus.setVille("Nimes");

		// Utiliser saveAndFlush pour s'assurer que l'objet est bien en base immédiatement
		nimesCampus = campusRepository.saveAndFlush(nimesCampus);
		System.out.println("-> 1. Campus créé : " + nimesCampus.getNomC());

		// 1.2 Créer un nouveau Bâtiment : 'bt1 nimes', 2024
		Batiment nimesBatiment = new Batiment();
		nimesBatiment.setCodeB("bt1 nimes"); // Clé primaire
		nimesBatiment.setAnneeConstruction(2024);

		// Associer le Bâtiment au Campus créé
		nimesBatiment.setCampus(nimesCampus);

		// Sauvegarder le Bâtiment
		nimesBatiment = batimentRepository.saveAndFlush(nimesBatiment);
		System.out.println("-> 2. Bâtiment créé et associé au Campus : " + nimesBatiment.getCodeB());

		// --- 2. Ajouter deux Salles associées au Bâtiment ---

		// Récupérer une référence au Batiment (comme demandé par l'énoncé)
		// Bien que 'nimesBatiment' soit déjà l'objet sauvé, ceci démontre l'utilisation de getReferenceById
		Batiment batimentReference = batimentRepository.getReferenceById("bt1 nimes");

		// 2.1 Créer la première Salle : un Amphi
		Salle amphiNimes = new Salle();
		amphiNimes.setNumSalle("A01");
		amphiNimes.setCapacite(200);
		amphiNimes.setTypeS(TypeSalle.AMPHI);
		amphiNimes.setEtage("0");
		amphiNimes.setAccessibilite("Oui");

		// Associer la Salle au Bâtiment de référence
		amphiNimes.setBatiment(batimentReference);
        salleRepository.saveAndFlush(amphiNimes);

		// 2.2 Créer la deuxième Salle : une salle de TP
		Salle tpNimes = new Salle();
		tpNimes.setNumSalle("TP101");
		tpNimes.setCapacite(30);
		tpNimes.setTypeS(TypeSalle.TP);
		tpNimes.setEtage("1");
		tpNimes.setAccessibilite("Non");

		// Associer la Salle au Bâtiment de référence
		tpNimes.setBatiment(batimentReference);
        salleRepository.saveAndFlush(tpNimes);

		System.out.println("-> 3. Deux Salles (A01 et TP101) ajoutées et associées au bâtiment 'bt1 nimes'.");
	}


	// Dans CL_Appli.java (à ajouter sous les autres méthodes de test)

	/**
	 * TP2 - Question 1 : De combien de bâtiments et de salles d'enseignement dispose chaque campus?
	 */
	public void tsCompterBatimentsEtSallesParCampus() {
		System.out.println("\n-> 1. Compte des Bâtiments par Campus:");

		List<Object[]> resultatsBat = gcs.compterBatimentsParCampus();
		if (resultatsBat.isEmpty()) {
			System.out.println("Aucun bâtiment trouvé.");
		} else {
			resultatsBat.forEach(res ->
					System.out.println("Campus: " + res[0] + " | Bâtiments: " + res[1])
			);
		}

		System.out.println("\n-> 1. Compte des Salles par Campus:");

		List<Object[]> resultatsSal = gcs.compterSallesParCampus();
		if (resultatsSal.isEmpty()) {
			System.out.println("Aucune salle trouvée.");
		} else {
			resultatsSal.forEach(res ->
					System.out.println("Campus: " + res[0] + " | Salles: " + res[1])
			);
		}
	}

	/**
	 * TP2 - Question 2 : Combien de salles de TD de moins de 40 places et accessibles aux PMR
	 * sont disponibles sur les campus situés à Montpellier?
	 */
	public void tsSallesTDSpecifiquesMontpellier() {
		System.out.println("\n-> 2. Salles de TD spécifiques à Montpellier (Cap < 40, Accessible) :");

		// Le service retourne le nombre (Long) directement.
		// Il n'y a pas besoin de List<Object[]> ou List<Salle> ici.
		Long compteSalles = gcs.compterTDSallesMontpellier();

		// Comme 'compteSalles' est un Long (un objet), il peut techniquement être null
		// si l'opération en base de données échoue sans lancer d'exception, mais c'est rare.
		// L'équivalent de 'isEmpty' ou 'size' est de vérifier si le nombre est 0.

		if (compteSalles == null || compteSalles == 0) {
			System.out.println("Nombre de salles trouvées : 0");
		} else {
			System.out.println("Nombre de salles trouvées : " + compteSalles);
		}

		// Remarque : Si vous voulez afficher la liste des salles, il faudrait une nouvelle méthode
		// dans GestionCampusService qui retourne List<Salle>.
	}

	// Dans CL_Appli.java, à l'intérieur de la méthode tsListerAmphisSpecifiques()

	// TP2 question 3
	public void tsListerAmphisSpecifiques() {
		String campusRecherche = "Triolet"; // Le nom du campus à tester
		int capaciteMinimale = 80;         // La capacité minimale

		System.out.println("\n-> 3. Amphis de " + campusRecherche + " avec au moins " + capaciteMinimale + " places :");

		// Correction de l'appel au service :
		List<Salle> amphis = gcs.findAmphisByCampusAndCapaciteMin(campusRecherche, capaciteMinimale);

		if (amphis.isEmpty()) {
			System.out.println("Aucun amphi trouvé correspondant aux critères.");
		} else {
			// Affichage des résultats
			amphis.forEach(salle -> System.out.println("  - " + salle.getNumSalle() + " (" + salle.getCapacite() + " places)"));
		}
	}

// Assurez-vous d'appeler tsListerAmphisSpecifiques() dans votre run() :
// tsListerAmphisSpecifiques();


	/**
	 * TP2 - Question 4 : Quelle est la capacité totale en places assises d'un campus/bâtiment?
	 */
	public void tsCapaciteTotale() {
		// Test avec le bâtiment que vous avez créé
		String codeBatiment = "bt1 nimes";
		String nomCampus = "FDE Nimes";

		System.out.println("\n-> 4. Capacité Totale du Bâtiment " + codeBatiment + ":");
		Long capBatiment = gcs.calculerCapaciteTotaleBatiment(codeBatiment);
		System.out.println("Capacité totale : " + capBatiment + " places.");

		System.out.println("\n-> 4. Capacité Totale du Campus " + nomCampus + ":");
		Long capCampus = gcs.calculerCapaciteTotaleCampus(nomCampus);
		System.out.println("Capacité totale : " + capCampus + " places.");
	}








	}
