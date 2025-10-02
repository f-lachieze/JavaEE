package org.example.ProjetJavaEE.Ex;

import java.util.ArrayList;
import java.util.List;

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
		 private BatimentRepository br;
		 @Autowired
		 private CampusRepository cr;

		public static void main(String[] args) {
			SpringApplication.run(CL_Appli.class, args);
		}
		
	public void run(String... args) throws Exception {
		tsLesBatiments();
		campusParVille("Montpellier");
		List<String> codes = new ArrayList<String>();
		codes.add("triolet_b36");
		codes.add("triolet_b16");
		certainsBatiments(codes);
	   }
	
	
	public void tsLesBatiments()
	{  Iterable<Batiment> e = br.findAll();
	  System.out.println("La liste des batiments");
      if (e != null) { 
   	   for (Batiment b : e)
          { System.out.println(b.getCodeB()+"\t "+b.getAnneeC()+"\t "+b.getCampus().getNomC());}
      } else System.out.println("nada");
	} 
	
	public void campusParVille(String ville)
	{  Iterable<Campus> e = cr.findByVille(ville);
	  System.out.println("La liste des campus de "+ville);
      if (e != null) { 
   	   for (Campus c : e)
          { System.out.println(c.toString());}
      } else System.out.println("nada");
	} 
	
	public void certainsBatiments(List<String> codes)
	{  Iterable<Batiment> e = br.findByIds(codes);
	  System.out.println("La liste de certains batiments");
      if (e != null) { 
   	   for (Batiment b : e)
          { System.out.println(b.getCodeB()+"\t "+b.getAnneeC()+"\t "+b.getCampus().getNomC());}
      } else System.out.println("nada");
	} 
}
