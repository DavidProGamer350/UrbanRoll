package com.parcial.app.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.parcial.app.entity.Vehiculo;


public interface VehiculoRepository extends MongoRepository<Vehiculo, String> {
	List<Vehiculo> findByEstado(String estado);
}
