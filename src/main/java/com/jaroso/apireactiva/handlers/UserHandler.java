package com.jaroso.apireactiva.handlers;

import com.jaroso.apireactiva.dto.UserLoginDTO;
import com.jaroso.apireactiva.dto.UserRegisterDTO;
import com.jaroso.apireactiva.entities.Producto;
import com.jaroso.apireactiva.entities.User;
import com.jaroso.apireactiva.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {

    @Autowired
    private UserService userService;

    public Mono<ServerResponse> register(ServerRequest request) {

        return request.bodyToMono(UserRegisterDTO.class)
                .flatMap(dto ->
                    // Verificamos si ya existe un usuario con ese nombre
                    userService.findByUsername(dto.username())
                            .flatMap(existingUser ->
                                    // Si existe, devolvemos una respuesta de error
                                    ServerResponse.badRequest()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .bodyValue("El usuario ya existe"))
                            .switchIfEmpty(
                                    // Si no existe, lo guardamos
                                    ServerResponse.ok()
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .body(userService.save(dto), User.class)
                                            //.bodyValue("Usuario registrado")
                            )
        );
    }

    public Mono<ServerResponse> login(ServerRequest request) {

        return request.bodyToMono(UserLoginDTO.class)
                .flatMap(dto ->
                    // Busca al usuario por username
                    userService.findByUsername(dto.username())
                            // Verifica que la contraseña proporcionada coincida con la almacenada
                            .filter(user -> userService.passwordMatches(dto.password(), user.getPassword()))
                            .flatMap(user -> {
                                // Si coincide, genera un token y lo retorna, pendiente de hacer

                                // Retorna la respuesta con el usuario
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(user);
                            })
                            // Si no se encontró el usuario o la contraseña no coincide, retorna UNAUTHORIZED
                            .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
                );
    }

}
