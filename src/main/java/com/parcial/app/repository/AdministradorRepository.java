package com.parcial.app.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.parcial.app.entity.Administrador;

public interface AdministradorRepository extends MongoRepository<Administrador, String>{
	
	Administrador findByCorreoAndContrasena(String correo, String contrasena);

}
