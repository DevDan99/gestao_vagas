package br.com.danielcosta.gestao_vagas.modules.candidate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielcosta.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.danielcosta.gestao_vagas.modules.candidate.useCases.AuthCandidateUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/candidate")
@Tag(name = "Candidato", description = "Informações do candidato")
public class AuthCandidateController {

	@Autowired
	private AuthCandidateUseCase authCandidateUseCase;

	@PostMapping("/auth")
	@Operation(summary = "Autorização para Usuário", description = "Cria autorização do usuário candidato.")
	public ResponseEntity<Object> authenticate(@RequestBody AuthCandidateRequestDTO authRequest) {

		try {

			var token = this.authCandidateUseCase.execute(authRequest);
			return ResponseEntity.ok().body(token);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
