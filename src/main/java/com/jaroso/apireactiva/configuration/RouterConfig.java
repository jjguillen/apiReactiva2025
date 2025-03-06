package com.jaroso.apireactiva.configuration;

import com.jaroso.apireactiva.handlers.ProductoHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.logging.Handler;

@Configuration
@EnableWebFlux
public class RouterConfig implements WebFluxConfigurer {

    @Bean
    public RouterFunction<ServerResponse> rutas(ProductoHandler handler) {

        return RouterFunctions.route()
                .GET("/api/productos", handler::listar)
                .GET("/api/productos/{id}", handler::ver)
                .POST("/api/productos", handler::crear)
                .PUT("/api/productos/{id}", handler::editar)
                .DELETE("/api/productos/{id}", handler::eliminar)
                .build();
    }

    /**
     * Configuración Cors, viene de implementar el interfaz WebFluxConfigurer y sobreescribir el método addCorsMappings
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("https://domain2.com")   //Aquí ponemos el dominio desde el que aceptamos peticiones
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }


}
