package br.com.danielcosta.gestao_vagas.modules.candidate.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.danielcosta.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.danielcosta.gestao_vagas.modules.candidate.useCases.CreateCandidateUseCase;
import br.com.danielcosta.gestao_vagas.modules.candidate.useCases.ListAllJobsByFilterUseCase;
import br.com.danielcosta.gestao_vagas.modules.candidate.useCases.ProfileCandidateUseCase;
import br.com.danielcosta.gestao_vagas.modules.company.entities.JobEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/candidate")
// Anotação Tag para agrupar endpoints relacionados na documentação.
@Tag(name = "Candidato", description = "Informações do candidato")
public class CandidateController {

    @Autowired // Injeção de dependência do Spring.
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @PostMapping("/")
    @Operation(summary = "Cadastro de candidato", description = "Cria um novo candidato na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "User already exists")
    })
    // Este método cria um novo candidato. e retorna uma resposta HTTP.
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) {
        // System.out.println(String.format("Candidato: %s criado com sucesso!", candidate.getEmail()));
        try {
            var result = this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Anotação que indica que esse método responde a requisições HTTP GET no endpoint "/candidate/".
    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do Usuário", description = "Exibe o perfil do usuário candidato.")
    @ApiResponses({
            // Anotação ApiResponse para descrever a resposta de sucesso (código 200).
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = ProfileCandidateResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "User not found")
    })
    @SecurityRequirement(name = "jwt_auth")

    public ResponseEntity<Object> get(HttpServletRequest request) {

        var idCandidate = request.getAttribute("candidate_id");

        try {
            var profile = this.profileCandidateUseCase.execute(UUID.fromString(idCandidate.toString()));
            return ResponseEntity.ok().body(profile);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    /* *** Documentação Swagger *** */
    // Anotação Operation para descrever a operação do endpoint.
    @Operation(summary = "Listar vagas por filtro", description = "Lista todas as vagas que correspondem ao filtro fornecido na descrição.")
    // Anotação ApiResponses para descrever as possíveis respostas do endpoint.
    @ApiResponses({
            // Anotação ApiResponse para descrever a resposta de sucesso (código 200).
            @ApiResponse(responseCode = "200", content = {
                    // Anotação Content para descrever o conteúdo da resposta.
                    // Anotação ArraySchema para descrever um array de objetos na resposta.
                    // Anotação Schema para descrever o esquema do objeto na resposta.
                    @Content(array = @ArraySchema(schema = @Schema(implementation = JobEntity.class)))
            })
    })
    @SecurityRequirement(name = "jwt_auth") // Anotação para indicar que este endpoint requer autenticação com o esquema "bearerAuth".)
    public List<JobEntity> findJobByFilter(@RequestParam String filter) {
        return this.listAllJobsByFilterUseCase.execute(filter);
    }
}
