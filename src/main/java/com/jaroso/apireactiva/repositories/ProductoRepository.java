package com.jaroso.apireactiva.repositories;

import com.jaroso.apireactiva.entities.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {


}
