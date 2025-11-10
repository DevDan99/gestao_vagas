package br.com.danielcosta.gestao_vagas.modules.company.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateJobDTO {

	@Schema(description = "Descrição da vaga", example = "Desenvolvedor Java Pleno", requiredMode = RequiredMode.REQUIRED)
	private String description;

	@Schema(description = "Benefícios da vaga", example = "Vale refeição, Vale transporte", requiredMode = RequiredMode.REQUIRED)
	private String benefits;

	@Schema(description = "Nível da vaga", example = "Pleno", requiredMode = RequiredMode.REQUIRED)
	private String level;
}
