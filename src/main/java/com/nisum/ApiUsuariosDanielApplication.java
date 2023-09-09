package com.nisum;

import com.nisum.utils.config.SwaggerConfiguration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@OpenAPIDefinition(
		info = @Info(
				title = "Documentación de API para prueba de Nisum"
		)
)
@EnableWebMvc
@SpringBootApplication
@Import(SwaggerConfiguration.class)
public class ApiUsuariosDanielApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiUsuariosDanielApplication.class, args);
	}

}
