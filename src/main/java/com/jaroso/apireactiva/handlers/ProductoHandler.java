package com.jaroso.apireactiva.handlers;

import com.jaroso.apireactiva.entities.Producto;
import com.jaroso.apireactiva.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductoHandler {

    private final ProductoRepository repository;

    public ProductoHandler(ProductoRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> listar(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(repository.findAll(), Producto.class);
    }

    //ServerResponse es reactivo, por lo que se puede usar flatMap para pasar de Mono<Producto> a Mono<ServerResponse>
    public Mono<ServerResponse> ver(ServerRequest request) {
        String id = request.pathVariable("id");
        return repository.findById(id)
                .flatMap(p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(p))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> crear(ServerRequest request) {
        Mono<Producto> producto = request.bodyToMono(Producto.class); //No hace falta pasarle id en el Json de entrada
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(repository.saveAll(producto), Producto.class);  //Guardamos el producto en MongoDB
    }

    public Mono<ServerResponse> editar(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Producto> producto = request.bodyToMono(Producto.class);
        Mono<Producto> productoDB = repository.findById(id);
        return productoDB.zipWith(producto, (db, req) -> {  //Modificamos el objeto producto con el Json
                    db.setNombre(req.getNombre());
                    db.setPrecio(req.getPrecio());
                    return db;
                }).flatMap(p -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(repository.save(p), Producto.class))   //Guardamos el producto modificado en MongoDB
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> eliminar(ServerRequest request) {
        String id = request.pathVariable("id");
        Mono<Producto> productoDB = repository.findById(id);  //Buscamos el producto en MongoDB
        return productoDB.flatMap(p -> repository.delete(p)  //Borramos el producto de MongoDB
                .then(ServerResponse.noContent().build()))  //Respuesta sin cuerpo si se ha borrado bien
                .switchIfEmpty(ServerResponse.notFound().build());  //Respuesta que no se ha encontrado el producto
    }

}
