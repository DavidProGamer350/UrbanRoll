package com.parcial.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.parcial.app.entity.Trabajador;

public interface TrabajadorRepository extends MongoRepository<Trabajador, String>{

	Trabajador findByCorreoAndContrasena(String correo, String contrasena);
	
}
