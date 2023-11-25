package com.parcial.app.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.parcial.app.entity.Vehiculo;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.VehiculoRepository;

@RestController
@RequestMapping(value = "/api/vehiculos")
public class VehiculoController {

	@Autowired
	private VehiculoRepository vehiculoRepository;

	@GetMapping("/")
	public List<Vehiculo> getAllVehiculos() {
		return vehiculoRepository.findAll();
	}

	@GetMapping("/{id}")
	public Vehiculo getVehiculoById(@PathVariable String id) {
		return vehiculoRepository.findById(id).orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));
	}

	@PostMapping("/")
	public Vehiculo saveVehiculo(@RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Vehiculo vehiculo = mapper.convertValue(body, Vehiculo.class);
		return vehiculoRepository.save(vehiculo);
	}

	@PutMapping("/{id}")
	public Vehiculo updateVehiculo(@PathVariable String id, @RequestBody Map<String, Object> body) {
		ObjectMapper mapper = new ObjectMapper();
		Vehiculo vehiculo = mapper.convertValue(body, Vehiculo.class);
		vehiculo.setId(id);
		return vehiculoRepository.save(vehiculo);
	}

	@DeleteMapping("/{id}")
	public Vehiculo deleteVehiculo(@PathVariable String id) {
		Vehiculo vehiculo = vehiculoRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("Vehiculo no encontrado"));
		vehiculoRepository.deleteById(id);
		return vehiculo;
	}
}
