package com.jaroso.apireactiva.security;

import com.jaroso.apireactiva.handlers.ProductoHandler;
import com.jaroso.apireactiva.handlers.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
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

    @Bean
    public RouterFunction<ServerResponse> loginRutas(UserHandler handler) {
        return RouterFunctions.route()
                .POST("/api/users/login", handler::login)
                .POST("/api/users/register", handler::register)
                .build();
    }


    /**
     * Configuración de seguridad
     * Añadimos la seguridad a las rutas que empiecen por /api/**
     * Permitimos acceso a las rutas de productos sin autenticación --> Cambiar cuando generemos tokens
     * Permitimos acceso a las rutas de autenticación/registro sin autenticación
     * @param http
     * @return
     */
    @Bean
    SecurityWebFilterChain webHttpSecurity(ServerHttpSecurity http) {
        http
                .securityMatcher(new PathPatternParserServerWebExchangeMatcher("/api/**"))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/productos/**").permitAll()  // Rutas públicas para productos aún no tengo token, quitar luego
                        .pathMatchers("/api/users/**").permitAll()  // Rutas públicas para autenticación/registro
                        .anyExchange().authenticated()
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Configuración Cors, viene de implementar el interfaz WebFluxConfigurer y sobreescribir el método addCorsMappings
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://javierprofe.com")   //Aquí ponemos el dominio desde el que aceptamos peticiones
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true).maxAge(3600);

        // Add more mappings...
    }


}
