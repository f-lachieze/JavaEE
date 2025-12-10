package org.example.ProjetJavaEE.Ex;

import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    private ExploiteRepository exploiteRepository;




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


        // pour remplir bd

        insertProjectDataInitial();


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

// Dans CL_Appli.java

    @Transactional
    public void insertProjectDataInitial() {
        System.out.println("--- Démarrage de l'insertion du Jeu de Données Initial (Script Prof) ---");

        // 1. DÉPENDANCE NÉCESSAIRE : L'Université de Montpellier doit exister.
        // getReferenceById(acronyme) est utilisé car UM a été créé au début de run().
        Universite um = universiteRepository.getReferenceById("UM");

        // --- 2. CAMPUS (LIAISON UM OBLIGATOIRE) ---
        // Utilisation du nouveau constructeur (nomC, ville, um)
        Campus triolet = campusRepository.save(new Campus("Triolet", "Montpellier", um));
        Campus stPriest = campusRepository.save(new Campus("St Priest", "Montpellier", um));
        Campus pharmacie = campusRepository.save(new Campus("Pharmacie", "Montpellier", um));
        Campus richter = campusRepository.save(new Campus("Richter", "Montpellier", um));
        Campus fdeMende = campusRepository.save(new Campus("FDE Mende", "Mende", um));
        Campus medecineNimes = campusRepository.save(new Campus("Medecine Nimes", "Nimes", um));




        // --- 3. BÂTIMENT (Dépend de Campus) ---
        Batiment trioletB36 = batimentRepository.save(new Batiment("triolet_b36", 2019, triolet));
        Batiment trioletB16 = batimentRepository.save(new Batiment("triolet_b16", 1966, triolet));
        Batiment trioletB05 = batimentRepository.save(new Batiment("triolet_b05", 1964, triolet));
        Batiment stPriestB02 = batimentRepository.save(new Batiment("stPriest_b02", 1982, stPriest));



// ... (Après l'insertion de stPriestB02) ...



        System.out.println("-> Insertion des bâtiments factices pour les tests d'affichage...");

// --- Ajout des Bâtiments Manquants ---
        batimentRepository.save(new Batiment("bt_pharmacie", 2000, pharmacie));
        batimentRepository.save(new Batiment("bt_richter", 1995, richter));
        batimentRepository.save(new Batiment("bt_mende_A", 1970, fdeMende));
        batimentRepository.save(new Batiment("bt_nimes_P", 1990, medecineNimes));


        // --- 4. COMPOSANTE ---
        Composante fds = composanteRepository.save(new Composante("FDS", "Faculte des Sciences", "JM. Marin"));
        Composante iae = composanteRepository.save(new Composante("IAE", "Ecole Universitaire de Management", "E Houze"));
        Composante polytech = composanteRepository.save(new Composante("Polytech", "Polytech Montpellier", "L. Torres"));


        // --- 5. SALLE (Dépend de Batiment) ---
        // Les insertions des 29 salles sont à ajouter ici, en utilisant les ENUM TypeSalle.
        // Vous devez vous assurer que votre classe Salle a un constructeur complet et que votre ENUM TypeSalle est définie.

        salleRepository.save(new Salle("A36.03", 120, TypeSalle.AMPHI, "oui", "rdc", trioletB36));
        salleRepository.save(new Salle("A36.02", 120, TypeSalle.AMPHI, "oui", "rdc", trioletB36));
        salleRepository.save(new Salle("A36.01", 120, TypeSalle.AMPHI, "oui", "rdc", trioletB36));
        salleRepository.save(new Salle("TD36.202", 40, TypeSalle.NUMERIQUE, "oui", "2", trioletB36));
        salleRepository.save(new Salle("TD36.203", 40, TypeSalle.NUMERIQUE, "oui", "2", trioletB36));
        salleRepository.save(new Salle("TD36.204", 40, TypeSalle.NUMERIQUE, "oui", "2", trioletB36));
        salleRepository.save(new Salle("SC36.04", 80, TypeSalle.SC, "oui", "1", trioletB36));
        salleRepository.save(new Salle("TD36.101", 40, TypeSalle.TD, "oui", "1", trioletB36));
        salleRepository.save(new Salle("TD36.302", 40, TypeSalle.TD, "oui", "3", trioletB36));
        salleRepository.save(new Salle("TD36.402", 40, TypeSalle.TD, "oui", "4", trioletB36));
        salleRepository.save(new Salle("SC16.03", 120, TypeSalle.AMPHI, "oui", "rdc", trioletB16));
        salleRepository.save(new Salle("TD16.02", 18, TypeSalle.TD, "oui", "rdc", trioletB16));
        salleRepository.save(new Salle("TPDeptInfo", 40, TypeSalle.NUMERIQUE, "oui", "rdc", trioletB16));
        salleRepository.save(new Salle("TPBio", 40, TypeSalle.TP, "oui", "rdc", trioletB16));
        salleRepository.save(new Salle("SC16.05", 48, TypeSalle.SC, "oui", "rdc", trioletB16));
        salleRepository.save(new Salle("A5.02", 275, TypeSalle.AMPHI, "oui", "1", trioletB05));
        salleRepository.save(new Salle("TD5.125", 20, TypeSalle.NUMERIQUE, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.126", 31, TypeSalle.NUMERIQUE, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.210", 40, TypeSalle.NUMERIQUE, "oui", "1", trioletB05));
        salleRepository.save(new Salle("TD5.201", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.202", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.203", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.204", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.205", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.206", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.207", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.208", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("TD5.209", 40, TypeSalle.TD, "oui", "rdc", trioletB05));
        salleRepository.save(new Salle("A_JJMoreau", 114, TypeSalle.AMPHI, "oui", "1", stPriestB02));

        // --- 6. EXPLOITE (Liaison Many-to-Many) ---
        // Nécessite l'implémentation des entités ExploiteId et Exploite.
        // exploiteRepository.save(new Exploite(fds, trioletB16)); // Exploite(Composante, Batiment)


        exploiteRepository.save(new Exploite(fds, trioletB16));
        exploiteRepository.save(new Exploite(iae, trioletB16));
        exploiteRepository.save(new Exploite(fds, trioletB36));
        exploiteRepository.save(new Exploite(iae, trioletB05));

        System.out.println("--- Insertion du Jeu de Données Initial Terminé. ---");


        // --- Récupérer les références des Bâtiments factices (pour l'insertion des salles) ---
// Note: Ces références doivent exister à ce point du code.
        Batiment btMende = batimentRepository.getReferenceById("bt_mende_A");
        Batiment btNimesP = batimentRepository.getReferenceById("bt_nimes_P");
        Batiment btPharmacie = batimentRepository.getReferenceById("bt_pharmacie");
        Batiment btRichter = batimentRepository.getReferenceById("bt_richter");
        Batiment btPaulva = batimentRepository.getReferenceById("Elearning Center");

// --- AJOUT DES SALLES MINIMALES POUR GARANTIR LA COUVERTURE DES TESTS ---
        System.out.println("-> Ajout des salles minimales pour les nouveaux bâtiments...");

// Salle pour FDE Mende (Campus: FDE Mende)
        salleRepository.save(new Salle("S-Mende-A01", 60, TypeSalle.AMPHI, "oui", "rdc", btMende));

// Salle pour Medecine Nimes (Campus: Medecine Nimes)
        salleRepository.save(new Salle("S-MedNimes-A01", 100, TypeSalle.AMPHI, "oui", "rdc", btNimesP));

// Salle pour Pharmacie (Campus: Pharmacie)
        salleRepository.save(new Salle("S-Pharma-T1", 40, TypeSalle.TD, "non", "2", btPharmacie));

// Salle pour Richter (Campus: Richter)
        salleRepository.save(new Salle("S-Richter-C1", 30, TypeSalle.SC, "oui", "1", btRichter));

// Salle pour Paul Va (Campus: Paul va)
        salleRepository.save(new Salle("S-Paulva-B1", 30, TypeSalle.SC, "oui", "1", btPaulva));
    }






	}
