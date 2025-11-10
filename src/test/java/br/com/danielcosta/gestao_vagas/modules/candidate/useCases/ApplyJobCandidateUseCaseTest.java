package br.com.danielcosta.gestao_vagas.modules.candidate.useCases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import br.com.danielcosta.gestao_vagas.exceptions.JobNotFoundException;
import br.com.danielcosta.gestao_vagas.exceptions.UserNotFoundException;
import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.danielcosta.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.com.danielcosta.gestao_vagas.modules.candidate.repositories.ApplyJobRepository;
import br.com.danielcosta.gestao_vagas.modules.company.entities.JobEntity;
import br.com.danielcosta.gestao_vagas.modules.company.repositories.JobRepository;

//  Implementar testes
@ExtendWith(MockitoExtension.class) // define que a classe vai usar o Mockito para mockar objetos
public class ApplyJobCandidateUseCaseTest {

	/*
	 * Diferença de uso de @InjectMocks e @Mock
	 * 
	 * @InjectMocks é usado para criar uma instância da classe que está sendo testada,
	 * 
	 * @Mock é usado para criar objetos simulados (mocks) das dependências dessa classe.
	 */

	@InjectMocks // injeta as dependências necessárias usando mocks
	private ApplyJobCandidateUseCase applyJobCandidateUseCase;

	@Mock // cria um mock do repositório
	private CandidateRepository candidateRepository;

	@Mock
	private JobRepository jobRepository;

	@Mock
	private ApplyJobRepository applyJobRepository;

	// Testando cenário onde o candidato não é encontrado no sistema e lança a exceção UserNotFoundException
	@Test
	@DisplayName("Should not be able to apply to job with candidate not found")
	public void should_not_be_able_to_apply_to_job_with_candidate_not_found() {
		try {
			applyJobCandidateUseCase.execute(null, null);

		} catch (Exception e) {
			assertThat(e).isInstanceOf(UserNotFoundException.class);
		}
	}

	// testando cenário onde a vaga não é encontrada no sistema e lança a exceção JobNotFoundException
	@Test
	@DisplayName("Should not be able to apply to job with job not found")
	public void should_not_be_able_to_apply_to_job_with_job_not_found() {

		UUID idCandidate = UUID.randomUUID();

		var candidate = new CandidateEntity();
		candidate.setId(idCandidate);

		when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));

		try {
			applyJobCandidateUseCase.execute(idCandidate, null);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(JobNotFoundException.class);
		}
	}

	// Testando cenário de sucesso onde o candidato aplica para a vaga com sucesso
	@Test
	@DisplayName("Should be able to create a new apply job")
	public void should_be_able_create_a_new_apply_job() {
		UUID idCandidate = UUID.randomUUID();
		UUID idJob = UUID.randomUUID();

		// Cria Objeto ApplyJobEntity
		var applyJob = ApplyJobEntity.builder()
				.candidateId(idCandidate)
				.jobId(idJob)
				.build();

		// Insere o ID da aplicação da vaga
		var applyJobCreated = ApplyJobEntity.builder().id(UUID.randomUUID()).build();

		when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(new CandidateEntity()));
		when(jobRepository.findById(idJob)).thenReturn(Optional.of(new JobEntity()));

		when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

		// testa o método execute
		var result = applyJobCandidateUseCase.execute(idCandidate, idJob);
		assertThat(result).hasFieldOrProperty("id");
		assertNotNull(result.getId());
	}
}
