package com.jaroso.apireactiva;

import com.jaroso.apireactiva.entities.Producto;
import com.jaroso.apireactiva.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ApiReactivaApplication implements CommandLineRunner {

	@Autowired
	private ProductoRepository productoRepository;

	public static void main(String[] args) { SpringApplication.run(ApiReactivaApplication.class, args); }


	@Override
	public void run(String... args) throws Exception {

		/*
		//Datos de ejemplo
		Flux.just( new Producto("TV", 500.0),
						new Producto("Radio", 100.0),
						new Producto( "Laptop", 1000.0),
						new Producto( "Tablet", 400.0),
						new Producto("Smartphone", 300.0))
				.flatMap(producto -> productoRepository.save(producto))
				.subscribe(producto -> System.out.println("Producto insertado: " + producto));
		*/

	}
}
