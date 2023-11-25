package com.parcial.app.controller;

import java.util.List;

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
import com.parcial.app.entity.Prestamo;
import com.parcial.app.entity.Vehiculo;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;
import com.parcial.app.repository.ClienteRepository;
import com.parcial.app.repository.PrestamoRepository;
import com.parcial.app.repository.VehiculoRepository;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/clientes")
public class ClienteTemplateController {

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private PrestamoRepository prestamoRepository;

	@GetMapping("/new/{id}")
	public String ClienteNewByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("cliente", new Cliente());
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "cliente/clientes-form";
	}

	@GetMapping("/list/{id}")
	public String ClientesListByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("clientes", clienteRepository.findAll());
		return "cliente/clientes-list";
	}

	@PostMapping("/salvar/{id}")
	public String ClienteSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("cliente") Cliente cliente, HttpSession session, @PathVariable("id") String id,
			Model model) {
		if (cliente.getId().isEmpty()) {
			cliente.setId(null);
		}

		if (cliente.getEstado().isEmpty()) {
			cliente.setEstado("activo");
		}

		clienteRepository.save(cliente);

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("clientes", clienteRepository.findAll());
		return "cliente/clientes-list";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String ClienteEditByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));

		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		return "cliente/clientes-form";
	}

	@GetMapping("/delete/{id}/{id2}")
	public String ClienteDeleteByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {

		Administrador existingAdmin = administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
		model.addAttribute("administrador", existingAdmin);

		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
		List<Prestamo> prestamos = prestamoRepository.findByCliente(cliente);

		if (!prestamos.isEmpty()) {
			for (Prestamo prestamo : prestamos) {
				Vehiculo vehiculo = vehiculoRepository.findById(prestamo.getVehiculo().getId())
						.orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));
				vehiculo.setEstado("Disponible");
				vehiculoRepository.save(vehiculo);
				prestamoRepository.deleteById(prestamo.getId());
			}
		}

		clienteRepository.deleteById(id);

		clienteRepository.deleteById(id);
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("clientes", clienteRepository.findAll());
		return "cliente/clientes-list";
	}

	// metodos cliente perfil ------------------------------------------------

	@PostMapping("/modificarPerfil")
	public String ClienteSavePerfil(@ModelAttribute("cliente") Cliente cliente, HttpSession session, Model model,
			RedirectAttributes redirectAttributes) {
		if (cliente.getId().isEmpty()) {
			cliente.setId(null);
		}

		if (cliente.getEstado().isEmpty()) {
			cliente.setEstado("activo");
		}

		clienteRepository.save(cliente);

		redirectAttributes.addAttribute("successMessage", "Por su seguridad se finalizado la sesión.");
		return "redirect:/login/";

	}

	@GetMapping("/perfil/{id}")
	public String PerfilCliente(@ModelAttribute("cliente") Cliente cliente, @PathVariable("id") String id,
			HttpSession session, Model model) {

		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));

		cliente = (Cliente) session.getAttribute("cliente");

		session.setAttribute("cliente", cliente);

		return "perfiles/perfil-cliente";
	}

	@GetMapping("/perfilHome/{id}")
	public String PerfilClienteHome(@ModelAttribute("cliente") Cliente cliente, @PathVariable("id") String id,
			HttpSession session, Model model) {

		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));

		cliente = (Cliente) session.getAttribute("cliente");

		session.setAttribute("cliente", cliente);

		return "homes/home-cliente";
	}

	@GetMapping("/modificar/{id}")
	public String ClienteEditablePerfiltByAdministrador(@ModelAttribute("cliente") Cliente cliente,
			@PathVariable("id") String id, Model model, HttpSession session) {
		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));

		cliente = (Cliente) session.getAttribute("cliente");

		session.setAttribute("cliente", cliente);

		return "perfiles/perfil-cliente-editable";
	}

	@PostMapping("/guardar")
	public String ClienteSave(@ModelAttribute("cliente") Cliente cliente, Model model) {
		if (cliente.getId().isEmpty()) {
			cliente.setId(null);
		}

		if (cliente.getEstado().isEmpty()) {
			cliente.setEstado("activo");
		}

		clienteRepository.save(cliente);

		return "login";
	}

}
