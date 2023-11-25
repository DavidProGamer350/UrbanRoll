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
import com.parcial.app.entity.Administrador;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.AdministradorRepository;

@RestController
@RequestMapping(value = "/api/administradores")
public class AdministradorController {

	@Autowired
	private AdministradorRepository administradorRepository;
	
	@GetMapping("/")
    public List<Administrador> getAllAdministradores() {
        return administradorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Administrador getAdministradorById(@PathVariable String id) {
        return administradorRepository.findById(id).orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
    }

    @PostMapping("/")
    public Administrador saveAdministrador(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Administrador administrador = mapper.convertValue(body, Administrador.class);
        return administradorRepository.save(administrador);
    }

    @PutMapping("/{id}")
    public Administrador updateAdministrador(@PathVariable String id, @RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Administrador administrador = mapper.convertValue(body, Administrador.class);
        administrador.setId(id);
        return administradorRepository.save(administrador);
    }

    @DeleteMapping("/{id}")
    public Administrador deleteAdministrador(@PathVariable String id) {
    	Administrador administrador = administradorRepository.findById(id).orElseThrow(() -> new NotFoundException("Administrador no encontrado"));
    	administradorRepository.deleteById(id);
        return administrador;
    }
}

