package br.com.danielcosta.gestao_vagas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;

// Classe de configuração do Swagger/OpenAPI para a aplicação.
@Configuration
public class SwaggerConfig {

	// Define as configurações do OpenAPI, incluindo informações da API e esquema de segurança.
	@Bean
	public OpenAPI OpenAPI() {
		return new OpenAPI() // Cria uma nova instância do OpenAPI.
				.info(new Info() // Define as informações da API.
						.title("Gestão de Vagas API")
						.version("1.0")
						.description("API para gerenciamento de vagas de emprego e candidatos"))

				// Define o esquema de segurança para autenticação JWT.
				.schemaRequirement("jwt_auth", createSecurityScheme());

		// .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
		// .components(new Components().addSecuritySchemes("Bearer Authentication", createSecurityScheme()));
	}

	// Cria o esquema de segurança para autenticação via JWT.
	private SecurityScheme createSecurityScheme() {
		return new SecurityScheme()
				.name("jwt_auth")
				.type(SecurityScheme.Type.HTTP)
				.scheme("bearer")
				.bearerFormat("JWT");
	}
}
