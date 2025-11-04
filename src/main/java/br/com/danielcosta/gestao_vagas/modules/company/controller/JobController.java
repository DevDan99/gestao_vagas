package br.com.danielcosta.gestao_vagas.modules.company.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielcosta.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.danielcosta.gestao_vagas.modules.company.entities.JobEntity;
import br.com.danielcosta.gestao_vagas.modules.company.useCases.CreateJobUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/job")
@Tag(name = "Vagas", description = "Informações da vaga")
public class JobController {

	@Autowired
	private CreateJobUseCase createJobUseCase;

	@PostMapping("/")
	@PreAuthorize("hasRole('COMPANY')") // Garante que apenas usuários com a role 'COMPANY' possam acessar este endpoint
	// Anotação Operation para descrever a operação do endpoint.
	@Operation(summary = "Cadastros de Vagas", description = "Responsavel por cadastrar as vagas dentro da empresa.")
	// Anotação ApiResponses para descrever as possíveis respostas do endpoint.
	@ApiResponses({
			// Anotação ApiResponse para descrever a resposta de sucesso (código 200).
			@ApiResponse(responseCode = "200", content = {
					// Anotação Content para descrever o conteúdo da resposta.
					// Anotação ArraySchema para descrever um array de objetos na resposta.
					// Anotação Schema para descrever o esquema do objeto na resposta.
					@Content(schema = @Schema(implementation = JobEntity.class))
			})
	})
	@SecurityRequirement(name = "jwt_auth") // Indica que este endpoint requer autenticação via Bearer Token
	public JobEntity create(@Valid @RequestBody CreateJobDTO jobDTO, HttpServletRequest request) {
		var companyId = request.getAttribute("companyId"); // Pega o ID da empresa do atributo da requisição

		var jobEntity = JobEntity.builder() // Constrói a entidade JobEntity usando o padrão Builder
				.description(jobDTO.getDescription())
				.benefits(jobDTO.getBenefits())
				.level(jobDTO.getLevel())
				.companyId(UUID.fromString(companyId.toString())) // Define o ID da empresa na entidade
				.build();

		return this.createJobUseCase.execute(jobEntity);
	}
}
