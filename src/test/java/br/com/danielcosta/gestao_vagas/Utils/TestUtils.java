package br.com.danielcosta.gestao_vagas.Utils;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

	public static String objectToJson(Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException("Failed to convert object to JSON", e);
		}
	}

	public static String generateToken(UUID companyId, String secret) {
		Algorithm algorithm = Algorithm.HMAC256(secret);

		var expiresIn = Instant.now().plus(Duration.ofHours(2)); // Define a expiração do token para 2 horas a partir de agora

		var token = JWT.create().withIssuer("Javagas") // Define o emissor do token
				.withSubject(companyId.toString()) // withSubject recebe o id do usuário autenticado como String
				.withExpiresAt(expiresIn)
				.withClaim("roles", Arrays.asList("COMPANY")) // Adiciona uma reivindicação personalizada "roles" ao token
				.sign(algorithm); // Assina o token com o algoritmo e a chave secreta

		return token;
	}
}
