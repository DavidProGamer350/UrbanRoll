package com.parcial.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.parcial.app.entity.*;
import com.parcial.app.repository.AdministradorRepository;
import com.parcial.app.repository.ClienteRepository;
import com.parcial.app.repository.TrabajadorRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginTemplateController {

	@GetMapping("/")
	public String HomeTemplate(Model model) {
		return "login";

	}

	@GetMapping("/registro")
	public String RegistroTemplate(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "registro";

	}

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private TrabajadorRepository trabajadorRepository;

	@Autowired
	private AdministradorRepository administradorRepository;

	@PostMapping("/ingresar")
	public String login(@RequestParam("correo") String correo, @RequestParam("contrasena") String contrasena,
			Model model, HttpSession session) {

		List<Cliente> clienteList = clienteRepository.findAll();
		System.out.println(clienteList.get(0).getCorreo());
		Cliente cliente = clienteRepository.findByCorreoAndContrasena(correo, contrasena);
		if (cliente != null) {
			// Inicio de sesión exitoso, redirigir a la página de inicio
			System.out.println("Correo: " + cliente.getCorreo() + " Password:" + cliente.getContrasena());
			Cliente client = (Cliente) session.getAttribute("cliente");
			session.setAttribute("cliente", client);
			model.addAttribute("cliente", cliente);
			if (cliente.getEstado().equals("bloqueado")) {
				model.addAttribute("authenticationFailed", true);
				model.addAttribute("errorMessage", "Su cuenta esta bloqueada");
				return "/login";
			} else {
				return "homes/home-cliente"; // Nombre de la página de inicio (por ejemplo, "inicio.html")

			}

		}
		List<Trabajador> trabajadorList = trabajadorRepository.findAll();
		System.out.println(trabajadorList.get(0).getCorreo());
		Trabajador trabajador = trabajadorRepository.findByCorreoAndContrasena(correo, contrasena);
		if (trabajador != null) {
			// Inicio de sesión exitoso, redirigir a la página de inicio
			System.out.println("Correo: " + trabajador.getCorreo() + " Password:" + trabajador.getContrasena());
			Trabajador trabajad = (Trabajador) session.getAttribute("trabajador");
			session.setAttribute("trabajador", trabajad);
			model.addAttribute("trabajador", trabajador);

			if (trabajador.getEstado().equals("bloqueado")) {
				model.addAttribute("authenticationFailed", true);
				model.addAttribute("errorMessage", "Su cuenta esta bloqueada");
				return "/login";
			} else {
				return "homes/home-trabajador"; // Nombre de la página de inicio (por ejemplo, "inicio.html")

			}

		}

		List<Administrador> administradorList = administradorRepository.findAll();
		System.out.println(administradorList.get(0).getCorreo());
		Administrador administrador = administradorRepository.findByCorreoAndContrasena(correo, contrasena);
		if (administrador != null) {
			// Inicio de sesión exitoso, redirigir a la página de inicio
			System.out.println("Correo: " + administrador.getCorreo() + " Password:" + administrador.getContrasena());
			Administrador admin = (Administrador) session.getAttribute("trabajador");
			session.setAttribute("administrador", admin);
			model.addAttribute("administrador", administrador);
			return "homes/home-administrador"; // Nombre de la página de inicio (por ejemplo, "inicio.html")

		}

		else if (cliente == null || trabajador == null || administrador == null) {
			// Inicio de sesión fallido, mostrar mensaje de error en la página de inicio
			model.addAttribute("authenticationFailed", true);
			model.addAttribute("errorMessage", "Usuario o contraseña incorrectos");
			return "/login";
		}
		return "/login";
	}

}
