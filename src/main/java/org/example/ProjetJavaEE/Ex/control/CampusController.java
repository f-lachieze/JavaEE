package org.example.ProjetJavaEE.Ex.control;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.example.ProjetJavaEE.Ex.service.impl.CampusServiceImpl;

@Controller
public class CampusController {

	@Autowired
	private CampusServiceImpl cs;

	@GetMapping("/listeCampus")
	public String home(	Model model) {
		model.addAttribute("campuses", cs.findAll());
		return "campuses";
	}

	public CampusController() {
		System.out.println("les campus ");
	}
}
