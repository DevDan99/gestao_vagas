
package br.com.danielcosta.gestao_vagas.modules.candidate.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.danielcosta.gestao_vagas.modules.candidate.dto.ProfileCandidateResponseDTO;
import br.com.danielcosta.gestao_vagas.modules.candidate.useCases.ApplyJobCandidateUseCase;
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
@Tag(name = "Candidato", description = "Informações do candidato") // Anotação Tag para agrupar endpoints relacionados na documentação.
public class CandidateController {

    @Autowired // Injeção de dependência do Spring.
    private CreateCandidateUseCase createCandidateUseCase;

    @Autowired
    private ProfileCandidateUseCase profileCandidateUseCase;

    @Autowired
    private ListAllJobsByFilterUseCase listAllJobsByFilterUseCase;

    @Autowired
    private ApplyJobCandidateUseCase applyJobCandidateUseCase;

    /**
     * Cria um novo candidato na plataforma.
     * 
     * @param candidate
     *            Entidade do candidato recebida no corpo da requisição.
     * @return ResponseEntity com o resultado do cadastro ou erro.
     */
    @PostMapping("/")
    @Operation(summary = "Cadastro de candidato", description = "Cria um novo candidato na plataforma.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = CandidateEntity.class))
            }),
            @ApiResponse(responseCode = "400", description = "User already exists")
    })
    public ResponseEntity<Object> create(@Valid @RequestBody CandidateEntity candidate) {
        // System.out.println(String.format("Candidato: %s criado com sucesso!", candidate.getEmail()));
        try {
            var result = this.createCandidateUseCase.execute(candidate);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Exibe o perfil do usuário candidato autenticado.
     * 
     * @param request
     *            HttpServletRequest para recuperar o atributo candidate_id.
     * @return ResponseEntity com o perfil do candidato ou erro.
     */
    @GetMapping("/")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Perfil do Usuário", description = "Exibe o perfil do usuário candidato.")
    @ApiResponses({
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

    /**
     * Lista todas as vagas que correspondem ao filtro fornecido na descrição.
     * 
     * @param filter
     *            Filtro de busca para vagas.
     * @return Lista de JobEntity que correspondem ao filtro.
     */
    @GetMapping("/job")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Listar vagas por filtro", description = "Lista todas as vagas que correspondem ao filtro fornecido na descrição.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(array = @ArraySchema(schema = @Schema(implementation = JobEntity.class)))
            })
    })
    @SecurityRequirement(name = "jwt_auth") // Anotação para indicar que este endpoint requer autenticação com o esquema "bearerAuth".
    public List<JobEntity> findJobByFilter(@RequestParam String filter) {
        return this.listAllJobsByFilterUseCase.execute(filter);
    }

    /**
     * Cria uma nova candidatura a uma vaga específica para o candidato autenticado.
     * 
     * @param request
     *            HttpServletRequest para recuperar o atributo candidate_Id.
     * 
     * @param jobID
     *            UUID da vaga para a qual o candidato deseja se candidatar.
     * 
     * @return ResponseEntity com o resultado da candidatura ou erro.
     */
    @PostMapping("/job/apply")
    @PreAuthorize("hasRole('CANDIDATE')")
    @Operation(summary = "Candidatar-se a uma vaga", description = "Permite que o candidato se candidate a uma vaga específica.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Object.class))
            }),
            @ApiResponse(responseCode = "400", description = "Erro ao candidatar-se à vaga")
    })
    public ResponseEntity<Object> applyJob(HttpServletRequest request, @RequestBody UUID jobID) {
        try {
            var candidateId = request.getAttribute("candidate_Id");
            var result = this.applyJobCandidateUseCase.execute(UUID.fromString(candidateId.toString()), jobID);
            return ResponseEntity.ok().body(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
