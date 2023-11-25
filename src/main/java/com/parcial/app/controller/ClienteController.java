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
import com.parcial.app.entity.Cliente;
import com.parcial.app.exception.NotFoundException;
import com.parcial.app.repository.ClienteRepository;


@RestController
@RequestMapping(value = "/api/clientes")
public class ClienteController {
	
	@Autowired
    private ClienteRepository clienteRepository;
	
	@GetMapping("/")
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cliente getClienteById(@PathVariable String id) {
        return clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    }

    @PostMapping("/")
    public Cliente saveCliente(@RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Cliente cliente = mapper.convertValue(body, Cliente.class);
        return clienteRepository.save(cliente);
    }

    @PutMapping("/{id}")
    public Cliente updateCliente(@PathVariable String id, @RequestBody Map<String, Object> body) {
        ObjectMapper mapper = new ObjectMapper();
        Cliente cliente = mapper.convertValue(body, Cliente.class);
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }

    @DeleteMapping("/{id}")
    public Cliente deleteCliente(@PathVariable String id) {
    	Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new NotFoundException("Cliente no encontrado"));
    	clienteRepository.deleteById(id);
        return cliente;
    }

}
