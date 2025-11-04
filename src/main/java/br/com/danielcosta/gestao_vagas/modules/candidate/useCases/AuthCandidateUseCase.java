package br.com.danielcosta.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.danielcosta.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.danielcosta.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;

@Service
public class AuthCandidateUseCase {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${security.token.secret.candidate}")
	private String secretKey;

	public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO authCandidateRequestDTO) throws AuthenticationException {
		// Lógica de autenticação do candidato
		// Por exemplo, verificar se o username e a senha são válidos
		// Se forem válidos, retornar um token de autenticação ou uma resposta de sucesso
		// Caso contrário, lançar uma exceção ou retornar uma resposta de erro

		// Buscar o candidato pelo username
		var candidate = this.candidateRepository.findByUsername(authCandidateRequestDTO.username())
				.orElseThrow(() -> {
					throw new UsernameNotFoundException("Username/password incorrect");
				});

		// Verificar se a senha fornecida corresponde à senha armazenada
		var passwordMatches = this.passwordEncoder.matches(authCandidateRequestDTO.password(), candidate.getPassword());

		if (!passwordMatches) {
			throw new AuthenticationException();
		}

		// Gerar um token JWT para o candidato autenticado (usando a biblioteca java-jwt)
		Algorithm algorithm = Algorithm.HMAC256(secretKey); // Algoritmo de assinatura HMAC com SHA-256 e a chave secreta
		var expiresIn = Instant.now().plus(Duration.ofMinutes(10)); // Definindo o tempo de expiração do token (10 minutos a partir de agora)

		var token = JWT.create()
				.withIssuer("javagas") // Emissor do token
				.withSubject(candidate.getId().toString()) // Assunto do token (ID do candidato)
				.withClaim("roles", Arrays.asList("CANDIDATE")) // Reivindicações personalizadas (roles do candidato)
				.withExpiresAt(expiresIn) // Data de expiração do token (10 minutos a partir de agora)
				.sign(algorithm);// Assina o token com o algoritmo especificado

		var authCandidateResponse = AuthCandidateResponseDTO.builder()
				.access_Token(token)
				.expires_In(expiresIn.toEpochMilli())
				.build();

		return authCandidateResponse;
	}
}
