package com.parcial.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.parcial.app.entity.*;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;
import com.parcial.app.repository.ClienteRepository;
import com.parcial.app.repository.PrestamoRepository;
import com.parcial.app.repository.TrabajadorRepository;
import com.parcial.app.repository.VehiculoRepository;
import com.parcial.app.utilities.ListarPrestamosPdf;

import jakarta.servlet.http.HttpSession;

@Controller // Asegúrate de agregar la anotación @Controller
@RequestMapping("/prestamos")
public class PrestamoTemplateController {

	@Autowired
	private PrestamoRepository prestamoRepository;

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private TrabajadorRepository trabajadorRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@Autowired
	private ListarPrestamosPdf listarPrestamoPdf;

	// metodos para administrador

	@GetMapping("/list/{id}")
	public String PrestamoListByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		model.addAttribute("prestamos", prestamoRepository.findAll());
		return "prestamos/prestamos-list-administrador";
	}

	@GetMapping("/new/{id}")
	public String PrestamoNewByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, Model model, HttpSession session, Model modelCliente, Model modelVehiculo,
			Model modelTrabajador) {
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);

		List<Cliente> clientes = clienteRepository.findAll();
		List<Trabajador> trabajadores = trabajadorRepository.findAll();
		List<Vehiculo> vehiculos = vehiculoRepository.findByEstado("Disponible");

		modelCliente.addAttribute("clientes", clientes);
		modelTrabajador.addAttribute("trabajadores", trabajadores);
		modelVehiculo.addAttribute("vehiculos", vehiculos);

		model.addAttribute("prestamo", new Prestamo());
		return "prestamos/prestamos-form-administrador";
	}

	@PostMapping("/salvar/{id}")
	public String PrestamorSaveByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@ModelAttribute("prestamo") Prestamo prestamo, HttpSession session, @PathVariable("id") String id,
			Model model, Model alerta, RedirectAttributes redirectAttributes) {

		Vehiculo vehiculo = vehiculoRepository.findById(prestamo.getVehiculo().getId())
				.orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));
		Cliente cliente = clienteRepository.findById(prestamo.getCliente().getId())
				.orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
		List<Prestamo> prestamos = prestamoRepository.findByClienteAndEstado(cliente, "Activo");

		if (prestamo.getId().isEmpty()) {
			prestamo.setId(null);
		}

		if (prestamo.getEstado().isEmpty()) {
			prestamo.setEstado("Activo");
		}

		if (vehiculo.getEstado().equalsIgnoreCase("Disponible")) {
			vehiculo.setEstado("Ocupado");
		}

		if (prestamo.getEstado().equalsIgnoreCase("Finalizado")) {
			vehiculo.setEstado("Disponible");
		} else {
			vehiculo.setEstado("Ocupado");
		}

		// Verificar la variable de sesión para determinar si se está editando
		Boolean edicionEnCurso = (Boolean) session.getAttribute("edicionEnCurso");
		if (edicionEnCurso != null && edicionEnCurso) {
			vehiculoRepository.save(vehiculo);
			prestamoRepository.save(prestamo);
			// Eliminar la variable de sesión después de procesar
			session.removeAttribute("edicionEnCurso");
			model.addAttribute("administrador", administradorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
			administrador = (Administrador) session.getAttribute("administrador");
			session.setAttribute("administrador", administrador);
			model.addAttribute("prestamos", prestamoRepository.findAll());
			return "prestamos/prestamos-list-administrador";
		}

		if (!prestamos.isEmpty()) {
			alerta.addAttribute("alert", "El cliente tiene alquileres activos");
			model.addAttribute("administrador", administradorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
			administrador = (Administrador) session.getAttribute("administrador");
			session.setAttribute("administrador", administrador);
			model.addAttribute("prestamos", prestamoRepository.findAll());
			return "prestamos/prestamos-list-administrador";
		}

		vehiculoRepository.save(vehiculo);
		prestamoRepository.save(prestamo);
		model.addAttribute("administrador", administradorRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("prestamos", prestamoRepository.findAll());
		return "prestamos/prestamos-list-administrador";
	}

	@GetMapping("/editar/{id}/{id2}")
	public String PrestamoEditByAdministrador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session,
			Model modelCliente, Model modelVehiculo, Model modelTrabajador) {
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));

		List<Cliente> clientes = clienteRepository.findAll();
		List<Vehiculo> vehiculos = vehiculoRepository.findAll();
		List<Trabajador> trabajadores = trabajadorRepository.findAll();

		modelTrabajador.addAttribute("trabajadores", trabajadores);
		modelCliente.addAttribute("clientes", clientes);
		modelVehiculo.addAttribute("vehiculos", vehiculos);

		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		session.setAttribute("edicionEnCurso", true);
		model.addAttribute("prestamo",
				prestamoRepository.findById(id).orElseThrow(() -> new NotFoundException("Prestamo no encontrado")));
		return "prestamos/prestamos-form-administrador";
	}

	@GetMapping("/eliminar/{id}/{id2}")
	public String TrabajadorDeleteByTrabajador(@ModelAttribute("administrador") Administrador administrador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session) {
		trabajadorRepository.deleteById(id);
		model.addAttribute("administrador", administradorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Administrador no encontrado")));
		administrador = (Administrador) session.getAttribute("administrador");
		session.setAttribute("administrador", administrador);
		model.addAttribute("trabajadores", trabajadorRepository.findAll());

		Prestamo prestamo = prestamoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Alquiler no encontrado"));
		Vehiculo vehiculo = vehiculoRepository.findById(prestamo.getVehiculo().getId())
				.orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));

		if (vehiculo != null) {
			vehiculo.setEstado("Disponible");
			vehiculoRepository.save(vehiculo);
		}

		prestamoRepository.deleteById(id);

		return "prestamos/prestamos-list-administrador";
	}

	// metodos para administrador

	@GetMapping("/listTrabajador/{id}")
	public String PrestamoListByTrabajador(@ModelAttribute("trabajador") Trabajador trabajador,
			@PathVariable("id") String id, Model model, HttpSession session) {

		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));

		trabajador = (Trabajador) session.getAttribute("trabajador");
		session.setAttribute("trabajador", trabajador);

		model.addAttribute("prestamos", prestamoRepository.findAll());
		return "prestamos/prestamos-list-trabajador";
	}

	@GetMapping("/newTrabajador/{id}")
	public String PrestamoNewByTrabajador(@ModelAttribute("trabajador") Trabajador trabajador,
			@PathVariable("id") String id, Model model, HttpSession session, Model modelCliente, Model modelVehiculo,
			Model modelTrabajador) {
		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));
		trabajador = (Trabajador) session.getAttribute("trabajador");
		session.setAttribute("trabajador", trabajador);

		List<Cliente> clientes = clienteRepository.findAll();
		List<Vehiculo> vehiculos = vehiculoRepository.findByEstado("Disponible");

		modelCliente.addAttribute("clientes", clientes);
		modelVehiculo.addAttribute("vehiculos", vehiculos);

		model.addAttribute("prestamo", new Prestamo());
		return "prestamos/prestamos-form-trabajador";
	}

	@PostMapping("/salvarTrabajador/{id}")
	public String PrestamorSaveByTrabajador(@ModelAttribute("trabajador") Trabajador trabajador,
			@ModelAttribute("prestamo") Prestamo prestamo, HttpSession session, @PathVariable("id") String id,
			Model model, Model alerta, RedirectAttributes redirectAttributes) {

		Vehiculo vehiculo = vehiculoRepository.findById(prestamo.getVehiculo().getId())
				.orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));
		Cliente cliente = clienteRepository.findById(prestamo.getCliente().getId())
				.orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
		List<Prestamo> prestamos = prestamoRepository.findByClienteAndEstado(cliente, "Activo");

		if (prestamo.getId().isEmpty()) {
			prestamo.setId(null);
		}

		if (prestamo.getEstado().isEmpty()) {
			prestamo.setEstado("Activo");
		}

		if (vehiculo.getEstado().equalsIgnoreCase("Disponible")) {
			vehiculo.setEstado("Ocupado");
		}

		if (prestamo.getEstado().equalsIgnoreCase("Finalizado")) {
			vehiculo.setEstado("Disponible");
		} else {
			vehiculo.setEstado("Ocupado");
		}

		// Verificar la variable de sesión para determinar si se está editando
		Boolean edicionEnCurso = (Boolean) session.getAttribute("edicionEnCurso");
		if (edicionEnCurso != null && edicionEnCurso) {
			vehiculoRepository.save(vehiculo);
			prestamoRepository.save(prestamo);
			// Eliminar la variable de sesión después de procesar
			session.removeAttribute("edicionEnCurso");
			model.addAttribute("trabajador", trabajadorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));
			trabajador = (Trabajador) session.getAttribute("trabajador");
			session.setAttribute("trabajador", trabajador);
			model.addAttribute("prestamos", prestamoRepository.findAll());
			return "prestamos/prestamos-list-trabajador";
		}

		if (!prestamos.isEmpty()) {
			alerta.addAttribute("alert", "El cliente tiene alquileres activos");
			model.addAttribute("trabajador", trabajadorRepository.findById(id)
					.orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));
			trabajador = (Trabajador) session.getAttribute("trabajador");
			session.setAttribute("trabajador", trabajador);
			model.addAttribute("prestamos", prestamoRepository.findAll());
			return "prestamos/prestamos-list-trabajador";
		}

		vehiculoRepository.save(vehiculo);
		prestamoRepository.save(prestamo);
		model.addAttribute("trabajador",
				trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));
		trabajador = (Trabajador) session.getAttribute("trabajador");
		session.setAttribute("trabajador", trabajador);

		model.addAttribute("prestamos", prestamoRepository.findAll());
		return "prestamos/prestamos-list-trabajador";
	}

	@GetMapping("/editarTrabajador/{id}/{id2}")
	public String PrestamoEditByTrabajador(@ModelAttribute("trabajador") Trabajador trabajador,
			@PathVariable("id") String id, @PathVariable("id2") String id2, Model model, HttpSession session,
			Model modelCliente, Model modelVehiculo, Model modelTrabajador) {
		model.addAttribute("trabajador", trabajadorRepository.findById(id2)
				.orElseThrow(() -> new NotFoundException("Trabajador no encontrado")));

		List<Cliente> clientes = clienteRepository.findAll();
		List<Vehiculo> vehiculos = vehiculoRepository.findAll();

		modelCliente.addAttribute("clientes", clientes);
		modelVehiculo.addAttribute("vehiculos", vehiculos);

		trabajador = (Trabajador) session.getAttribute("trabajador");
		session.setAttribute("trabajador", trabajador);
		session.setAttribute("edicionEnCurso", true);

		model.addAttribute("prestamo",
				prestamoRepository.findById(id).orElseThrow(() -> new NotFoundException("Prestamo no encontrado")));
		return "prestamos/prestamos-form-trabajador";
	}

	// metodos para cliente

	@GetMapping("/listCliente/{id}")
	public String PrestamoListByCliente(@ModelAttribute("cliente") Cliente cliente, @PathVariable("id") String id,
			Model model, HttpSession session) {

		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));

		model.addAttribute("prestamos", prestamoRepository.findByCliente(cliente));
		cliente = (Cliente) session.getAttribute("cliente");
		session.setAttribute("cliente", cliente);

		return "prestamos/prestamos-list-cliente";
	}

	// Metodo para generar Pdf
	@GetMapping("/pdf/{id}")
	public ModelAndView generarPdf(@ModelAttribute("cliente") Cliente cliente, @PathVariable("id") String id,
			Model model, HttpSession session) {

		model.addAttribute("cliente",
				clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado")));
		ArrayList<Prestamo> listadoPrestamos = (ArrayList<Prestamo>) prestamoRepository.findByCliente(cliente);
		cliente = (Cliente) session.getAttribute("cliente");
		session.setAttribute("cliente", cliente);

		// Crea el modelo con los datos que deseas pasar a la vista PDF
		Map<String, Object> model1 = new HashMap<>();
		model1.put("prestamos", listadoPrestamos);

		return new ModelAndView(listarPrestamoPdf, model1);
	}

}
