package com.parcial.app.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.parcial.app.entity.*;

public interface PrestamoRepository extends MongoRepository<Prestamo, String> {
	List<Prestamo> findByClienteAndEstado(Cliente cliente, String estado);

	List<Prestamo> findByCliente(Cliente cliente);

	Prestamo findByVehiculo(Vehiculo vehiculo);

	@Query("{ 'prestamo.trabajador' : ?0 }")
	List<Prestamo> findByTrabajador(Trabajador trabajador);
}
