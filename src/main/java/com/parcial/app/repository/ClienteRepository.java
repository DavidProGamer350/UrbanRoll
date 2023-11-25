package com.parcial.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.parcial.app.entity.Cliente;
public interface ClienteRepository extends MongoRepository<Cliente, String> {

	Cliente findByCorreoAndContrasena(String correo, String contrasena);

}
