package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI configurarSwagger() {
    	return new OpenAPI().info(new io.swagger.v3.oas.models.info.Info().title("PlayWise")
    			.description("Projeto feito com intuito de indicar jogos")
				.summary("Este é um projeto para indicar jogos de acordo com os jogos avaliados por cada usuario")
				.version("1.0.0")
				.termsOfService("Você concorda em utilizar nossa aplicação?")
				.license(new License().url("pagina_licenca").name("Premium User"))
				);			
		}
}
