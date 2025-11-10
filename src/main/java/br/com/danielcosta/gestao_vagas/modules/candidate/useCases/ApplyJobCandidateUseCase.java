package br.com.danielcosta.gestao_vagas.modules.candidate.useCases;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielcosta.gestao_vagas.exceptions.JobNotFoundException;
import br.com.danielcosta.gestao_vagas.exceptions.UserNotFoundException;
import br.com.danielcosta.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.danielcosta.gestao_vagas.modules.candidate.entities.ApplyJobEntity;
import br.com.danielcosta.gestao_vagas.modules.candidate.repositories.ApplyJobRepository;
import br.com.danielcosta.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ApplyJobCandidateUseCase {

	@Autowired
	private CandidateRepository candidateRepository;

	@Autowired
	private JobRepository jobRepository;

	@Autowired
	private ApplyJobRepository applyJobRepository;

	public ApplyJobEntity execute(UUID candidateId, UUID jobId) {
		// Validar se candidato existe
		this.candidateRepository.findById(candidateId).orElseThrow(
				() -> {
					throw new UserNotFoundException();
				});

		// Validar se vaga existe
		this.jobRepository.findById(jobId).orElseThrow(
				() -> {
					throw new JobNotFoundException();
				});

		// Candidato aplicar na vaga
		var applyJob = ApplyJobEntity.builder()
				.candidateId(candidateId)
				.jobId(jobId)
				.build();

		applyJob = applyJobRepository.save(applyJob);
		return applyJob;
	}
}
