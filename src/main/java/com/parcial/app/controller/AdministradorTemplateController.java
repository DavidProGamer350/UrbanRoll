package com.parcial.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.parcial.app.entity.Administrador;
import com.parcial.app.entity.Trabajador;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/administradores")
public class AdministradorTemplateController {
	@Autowired
	private AdministradorRepository administradorRepository;

	// metodos para trabajador

	@GetMapping("/list/{id}")
	public String AdministradoresListByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("administradores", administradorRepository.findAll());
		return "administrador/administradores-list";
	}

	@GetMapping("/new/{id}")
	public String AdministradorNewByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administradores", administrador);
		model.addAttribute("administrador", new Administrador());
		return "administrador/administradores-form-new";
	}

	@PostMapping("/save")
	public String AdministradorSaveByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			Model model, RedirectAttributes redirectAttributes) {
		if (administrador.getId().isEmpty()) {
			administrador.setId(null);
		}

		administradorRepository.save(administrador);
		model.addAttribute("administradores", administradorRepository.findAll());
		redirectAttributes.addAttribute("successMessage", "Por seguridad, se finalizo sesión.");

		return "redirect:/login/";
	}

	@PostMapping("/saveNew")
	public String AdministradorSave(@ModelAttribute("administrador") Administrador administrador, Model model,
			RedirectAttributes redirectAttributes) {
		if (administrador.getId().isEmpty()) {
			administrador.setId(null);
		}

		administradorRepository.save(administrador);
		redirectAttributes.addAttribute("successMessage", "Por seguridad, se finalizo sesión.");

		return "redirect:/login/";
	}

	@GetMapping("/editar/{id}")
	public String AdministradorEditByTrabajador(@PathVariable("id") String id, Model model) {
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		return "administrador/administradores-form";
	}

	@GetMapping("/eliminar/{id}")
	public String AdministradorDeleteByTrabajador(@PathVariable("id") String id,
			RedirectAttributes redirectAttributes) {
		administradorRepository.deleteById(id);
		redirectAttributes.addAttribute("successMessage", "Por seguridad, se finalizo sesión.");

		return "redirect:/login/";
	}

	// metodos para administrador

	@GetMapping("/lista")
	public String AdministradoresListByAdministrador(Model model) {
		model.addAttribute("administradores", administradorRepository.findAll());
		return "administradores-list-administrador";
	}

	@GetMapping("/nuevo")
	public String AdministradorNewByAdministrador(Model model) {
		model.addAttribute("administrador", new Administrador());
		return "administradores-form-administrador";
	}

	@PostMapping("/salvar")
	public String AdministradorSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador) {
		if (administrador.getId().isEmpty()) {
			administrador.setId(null);
		}

		administradorRepository.save(administrador);
		return "redirect:/administradores/lista";
	}

	// metodos administrador perfil

	@PostMapping("/modificarPerfil")
	public String AdministradorSaveByPerfilAdministrador(@ModelAttribute("administrador") Administrador administrador,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (administrador.getId().isEmpty()) {
			administrador.setId(null);
		}

		administradorRepository.save(administrador);
		redirectAttributes.addAttribute("successMessage", "Por su seguridad se finalizado la sesión.");
		return "redirect:/login/";
	}

	@GetMapping("/perfil/{id}")
	public String PerfilCliente(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, HttpSession session, Model model) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");

		session.setAttribute("administrador", administrador);

		return "perfiles/perfil-administrador";
	}

	@GetMapping("/perfilHome/{id}")
	public String PerfilClienteHome(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, HttpSession session, Model model) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");

		session.setAttribute("administrador", administrador);

		return "homes/home-administrador";
	}

	@GetMapping("/modificar/{id}")
	public String AdministradorEditPerfilByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "perfiles/perfil-administrador-editable";
	}

}
