package com.parcial.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.parcial.app.entity.Administrador;
import com.parcial.app.entity.Cliente;
import com.parcial.app.entity.Vehiculo;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;
import com.parcial.app.repository.VehiculoRepository;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/vehiculos")
public class VehiculoTemplateController {

	@Autowired
	private VehiculoRepository vehiculoRepository;;

	@Autowired
	private AdministradorRepository administradorRepository;

	@GetMapping("/new/{id}")
	public String vehiculoNew(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("vehiculo", new Vehiculo());
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "vehiculos/vehiculos-form";
	}

	@GetMapping("/list/{id}")
	public String VehiculosListByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("vehiculos", vehiculoRepository.findAll());
		return "vehiculos/vehiculos-list";
	}

	@PostMapping("/salvar/{id}")
	public String VehiculosSave(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("vehiculo") Vehiculo vehiculo, HttpSession session, @PathVariable("id") String id,
			Model model) {
		if (vehiculo.getId().isEmpty()) {
			vehiculo.setId(null);
		}

		if (vehiculo.getEstado().isEmpty()) {
			vehiculo.setEstado("Disponible");
		}

		vehiculoRepository.save(vehiculo);

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("vehiculos", vehiculoRepository.findAll());
		return "vehiculos/vehiculos-list";
	}

	@PostMapping("/save")
	public String VehiculoSaveByTrabajador(@ModelAttribute("vehiculo") Vehiculo vehiculo,
			@RequestParam("file") MultipartFile imagen) throws IOException {
		if (vehiculo.getId().isEmpty()) {
			vehiculo.setId(null);
		}

		if (vehiculo.getEstado().isEmpty()) {
			vehiculo.setEstado("Disponible");
		}

		vehiculoRepository.save(vehiculo);
		return "redirect:/vehiculos/";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String VehiculoEdit(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		model.addAttribute("vehiculo",
				vehiculoRepository.findById(id).orElseThrow(() -> new NotFoundException("Vehiculo no encontrado")));

		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "vehiculos/vehiculos-form";
	}

	@GetMapping("/delete/{id}/{id2}")
	public String ClienteDeleteByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		vehiculoRepository.deleteById(id);
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("vehiculos", vehiculoRepository.findAll());
		return "vehiculos/vehiculos-list";
	}

}
