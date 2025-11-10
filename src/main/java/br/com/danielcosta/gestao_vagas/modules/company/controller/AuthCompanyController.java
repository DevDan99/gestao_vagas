package br.com.danielcosta.gestao_vagas.modules.company.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielcosta.gestao_vagas.modules.company.dto.AuthCompanyDTO;
import br.com.danielcosta.gestao_vagas.modules.company.useCases.AuthCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/company")
@Tag(name = "Autorização Company/Job", description = "Autenticação da empresa/vagas")
public class AuthCompanyController {

	@Autowired
	private AuthCompanyUseCase authCompanyUseCase;

	@PostMapping("/auth")
	@Operation(summary = "Autorização para Empresa/vagas", description = "Cria Token de autorização da empresa/vagas.")
	public ResponseEntity<Object> create(@RequestBody AuthCompanyDTO authCompanyDTO) {
		try {
			var result = this.authCompanyUseCase.execute(authCompanyDTO);
			return ResponseEntity.ok().body(result);

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		}
	}
}
