package br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.fatecmaua.trabalho3sem.indicacao_de_jogos.model.Usuario;

@Service
public class TokenService {
	
	@Value("${api.security.token.secret}")
	private String secret;
	
	public String gerarToken(Usuario usuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			String token = JWT.create()
					.withIssuer("sistema-jogos")
					.withSubject(usuario.getEmail())
					.withExpiresAt(generationExpirationDate())
					.sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Erro ao gerar token: ", e);
		}
	}
	
	public String validarToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			return JWT.require(algorithm)
					.withIssuer("sistema-jogos")
					.build()
					.verify(token)
					.getSubject();
					
		} catch (JWTVerificationException e) {
			return "";
		}
	}
	
	private Instant generationExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}

