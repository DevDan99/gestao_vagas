package br.com.danielcosta.gestao_vagas.modules.candidate.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCandidateResponseDTO {

	@Schema(example = "Desenvolvedor Java")
	private String description;

	@Schema(example = "João")
	private String username;

	@Schema(example = "joao@gmail.com")
	private String email;

	private String id;

	@Schema(example = "João da Silva")
	private String name;

}
