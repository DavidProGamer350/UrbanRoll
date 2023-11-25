package com.parcial.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeTemplateController {

	@GetMapping("/")
	public String homeTemplate(Model model) {

		return "home";

	}

	@GetMapping("/cliente")
	public String homeClienteTemplate(Model model, HttpSession session) {

		return "homes/home-cliente";

	}

	@GetMapping("/trabajador")
	public String homeTrabajadorTemplate(Model model, HttpSession session) {

		return "homes/home-trabajador";

	}

	@GetMapping("/administrador")
	public String homeAdministradorTemplate(Model model, HttpSession session) {

		return "homes/home-administrador";

	}

}
