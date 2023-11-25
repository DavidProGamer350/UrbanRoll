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
import com.parcial.app.entity.Cliente;
import com.parcial.app.entity.Trabajador;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;
import com.parcial.app.repository.TrabajadorRepository;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/trabajadores")
public class TrabajadorTemplateController {
	@Autowired
	private TrabajadorRepository trabajadorRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@GetMapping("/list/{id}")
	public String TrabajadoresListByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("trabajadores", trabajadorRepository.findAll());
		return "trabajador/trabajadores-list";
	}

	@GetMapping("/new/{id}")
	public String TrabajadorNewByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("trabajadores", trabajadorRepository.findAll());
		model.addAttribute("trabajador", new Trabajador());
		return "trabajador/trabajadores-form";
	}

	@PostMapping("/salvar/{id}")
	public String TrabajadorSaveByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("trabajador") Trabajador trabajador, HttpSession session, @PathVariable("id") String id,
			Model model) {
		if (trabajador.getId().isEmpty()) {
			trabajador.setId(null);
		}

		if (trabajador.getEstado().isEmpty()) {
			trabajador.setEstado("activo");
		}

		trabajadorRepository.save(trabajador);

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("trabajadores", trabajadorRepository.findAll());
		return "trabajador/trabajadores-list";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String TrabajadorEditByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));

		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "trabajador/trabajadores-form";
	}

	@GetMapping("/delete/{id}/{id2}")
	public String TrabajadorDeleteByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		trabajadorRepository.deleteById(id);

		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("trabajadores", trabajadorRepository.findAll());
		return "trabajador/trabajadores-list";
	}

	// metodos trabajador perfil

	@PostMapping("/modificarPerfil")
	public String TrabajadorSavePerfilByAdministrador(@ModelAttribute("trabajador") Trabajador trabajador,
			RedirectAttributes redirectAttributes, HttpSession session) {
		if (trabajador.getId().isEmpty()) {
			trabajador.setId(null);
		}

		if (trabajador.getEstado().isEmpty()) {
			trabajador.setEstado("activo");
		}

		trabajadorRepository.save(trabajador);

		redirectAttributes.addAttribute("successMessage", "Por su seguridad se finalizado la sesión.");
		return "redirect:/login/";
	}

	@GetMapping("/perfil/{id}")
	public String PerfilCliente(@ModelAttribute("trabajador") Trabajador trabajador, @PathVariable("id") String id,
			HttpSession session, Model model) {

		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));

		trabajador = (Trabajador) session.getAttribute("trabajador");

		session.setAttribute("trabajador", trabajador);

		return "perfiles/perfil-trabajador";
	}

	@GetMapping("/perfilHome/{id}")
	public String PerfilClienteHome(@ModelAttribute("trabajador") Trabajador trabajador, @PathVariable("id") String id,
			HttpSession session, Model model) {

		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));

		trabajador = (Trabajador) session.getAttribute("trabajador");

		session.setAttribute("trabajador", trabajador);

		return "homes/home-trabajador";
	}

	@GetMapping("/modificar/{id}")
	public String TrabajadorEditPerfilByAdministrador(@ModelAttribute("trabajador") Trabajador trabajador,
			@PathVariable("id") String id, HttpSession session, Model model) {
		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));
		trabajador = (Trabajador) session.getAttribute("trabajador");

		session.setAttribute("trabajador", trabajador);

		return "perfiles/perfil-trabajador-editable";
	}

}
