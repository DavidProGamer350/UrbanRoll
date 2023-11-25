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
import com.parcial.app.entity.Trabajador;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.TrabajadorRepository;

@RestController
@RequestMapping(value = "/api/trabajadores")
public class TrabajadorController {

	@Autowired
	private TrabajadorRepository trabajadorRepository;
	
	@GetMapping("/")
    public List<Trabajador> getAllEmpleados() {
        return trabajadorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Trabajador getTrabajadorById(@PathVariable String id) {
        return trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado"));
    }

    @PostMapping("/")
    public Trabajador saveTrabajador(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Trabajador trabajador = mapper.convertValue(body, Trabajador.class);
        return trabajadorRepository.save(trabajador);
    }

    @PutMapping("/{id}")
    public Trabajador updateTrabajador(@PathVariable String id, @RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Trabajador trabajador = mapper.convertValue(body, Trabajador.class);
        trabajador.setId(id);
        return trabajadorRepository.save(trabajador);
    }

    @DeleteMapping("/{id}")
    public Trabajador deleteTrabajador(@PathVariable String id) {
    	Trabajador trabajador = trabajadorRepository.findById(id).orElseThrow(() -> new NotFoundException("Trabajador no encontrado"));
    	trabajadorRepository.deleteById(id);
        return trabajador;
    }
}
