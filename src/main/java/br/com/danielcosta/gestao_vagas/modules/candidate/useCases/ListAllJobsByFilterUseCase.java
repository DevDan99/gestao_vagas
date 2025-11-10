package br.com.danielcosta.gestao_vagas.modules.candidate.useCases;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.danielcosta.gestao_vagas.modules.company.entities.JobEntity;
import br.com.danielcosta.gestao_vagas.modules.company.repositories.JobRepository;

@Service
public class ListAllJobsByFilterUseCase {

	@Autowired
	private JobRepository jobRepository;

	public List<JobEntity> execute(String filter) {

		System.out.println("Filter received: " + filter);

		List<JobEntity> jobs = this.jobRepository.findByDescriptionContainingIgnoreCase(filter);
		if (jobs == null || jobs.isEmpty()) {
			throw new RuntimeException("No jobs found with the given filter");
		}
		return jobs;
	}
}