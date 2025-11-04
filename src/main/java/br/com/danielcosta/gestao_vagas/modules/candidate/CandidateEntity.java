package br.com.danielcosta.gestao_vagas.modules.candidate;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "candidates") // Entity vai mapear a classe para uma tabela no banco de dados.
public class CandidateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Geração automática do ID.
    private UUID id;

    @Schema(example = "João da Silva", requiredMode = RequiredMode.REQUIRED)
    private String name;

    // Validação de regex usando dependência dospring-boot-starter-validation.
    @Pattern(regexp = "\\S+", message = "O campo [username] não deve conter espaços")
    @Schema(example = "joaosilva", requiredMode = RequiredMode.REQUIRED)
    private String username;

    // Validação de email usando dependência do spring-boot-starter-validation.
    @Email(message = "Deve conter um email válido")
    @Schema(example = "joao.silva@email.com", requiredMode = RequiredMode.REQUIRED)
    private String email;

    // Validação de tamanho usando dependência do spring-boot-starter-validation.
    @Length(min = 10, max = 100, message = "A senha deve conter no mínimo 10 e no máximo 100 caracteres")
    @Schema(example = "senhaSegura123", minLength = 10, maxLength = 100, requiredMode = RequiredMode.REQUIRED)
    private String password;

    @Schema(example = "Desenvolvedor Java", requiredMode = RequiredMode.NOT_REQUIRED)
    private String description;
    private String curriculum;

    @CreationTimestamp // Anotação do Hibernate para definir a data de criação automática.
    private LocalDateTime createdAt;
}
