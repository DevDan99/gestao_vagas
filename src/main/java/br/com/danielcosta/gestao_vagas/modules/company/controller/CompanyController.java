package br.com.danielcosta.gestao_vagas.modules.company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielcosta.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.danielcosta.gestao_vagas.modules.company.useCases.CreateCompanyUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company")
@Tag(name = "Empresas", description = "Informações da empresa")
public class CompanyController {

	@Autowired
	private CreateCompanyUseCase createCompanyUseCase;

	@PostMapping("/")
	@Operation(summary = "Cadastros de Empresas", description = "Responsavel por cadastrar as empresas.")
	@ApiResponses({
			// Anotação ApiResponse para descrever a resposta de sucesso (código 200).
			@ApiResponse(responseCode = "200", content = {
					@Content(schema = @Schema(implementation = CompanyEntity.class))
			}),
			@ApiResponse(responseCode = "400", description = "User already exists")
	})
	public ResponseEntity<Object> create(@Valid @RequestBody CompanyEntity company) {
		try {

			var result = this.createCompanyUseCase.execute(company);
			return ResponseEntity.ok().body(result);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

}
