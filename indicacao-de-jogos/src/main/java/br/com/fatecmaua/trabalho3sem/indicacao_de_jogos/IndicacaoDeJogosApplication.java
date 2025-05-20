package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@EnableCaching
@EnableJpaRepositories
@ComponentScan
@EntityScan
@SpringBootApplication
public class IndicacaoDeJogosApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndicacaoDeJogosApplication.class, args);
	}

}
