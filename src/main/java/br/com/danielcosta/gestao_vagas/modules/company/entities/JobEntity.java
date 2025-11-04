package br.com.danielcosta.gestao_vagas.modules.company.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Classe de entidade que representa uma vaga de emprego no sistema de gestão de vagas.
@Entity(name = "job") // Anotação JPA para mapear esta classe para a tabela "job" no banco de dados.
@Data // Anotação do Lombok para gerar getters, setters, toString, equals e hashCode automaticamente.
@Builder // Anotação do Lombok para implementar o padrão Builder.
@AllArgsConstructor // Construtor com todos os argumentos necessário para o Lombok.
@NoArgsConstructor // Construtor sem argumentos necessário para o JPA.
public class JobEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID) // Geração automática de UUID para o campo id.
	private UUID id;

	@Schema(description = "Descrição da vaga", example = "Vaga Para Desenvolvedor Java Pleno") // Anotação para documentação OpenAPI (Swagger)
	private String description;

	@Schema(description = "Benefícios da vaga", example = "Vale Refeição, Plano de Saúde") // Anotação para documentação OpenAPI (Swagger)
	private String benefits;

	@NotBlank(message = "O campo é obrigatório.") // Validação para garantir que o campo não esteja em branco.
	@Schema(description = "Nível da vaga", example = "Júnior, Pleno, Sênior") // Anotação para documentação OpenAPI (Swagger)
	private String level;

	@ManyToOne() // Relacionamento muitos-para-um com a entidade CompanyEntity.
	@JoinColumn(name = "company_id", insertable = false, updatable = false) // Especifica que o relacionamento será feito através da coluna company_id na tabela job.
	private CompanyEntity companyEntity; // Este campo representa o objeto empresa relacionado à vaga.

	@Column(name = "company_id") // Mapeia o campo companyId para a coluna "company_id" na tabela "job".
	private UUID companyId;

	@CreationTimestamp // Anotação Hibernate para definir automaticamente o timestamp de criação.
	private LocalDateTime createdAt;
}
