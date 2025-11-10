package br.com.danielcosta.gestao_vagas.modules.company.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.danielcosta.gestao_vagas.Utils.TestUtils;
import br.com.danielcosta.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.danielcosta.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.danielcosta.gestao_vagas.modules.company.repositories.CompanyRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Inicia o contexto da aplicação Spring para testes.
@ActiveProfiles("test") // Usa o perfil "test" para este teste.
public class CreateJobControllerTest {
	private MockMvc mvc; // Simula requisições HTTP para testes de controladores Spring.

	@Autowired
	private WebApplicationContext context; // Contexto da aplicação web do Spring.

	@Autowired
	private CompanyRepository companyRepository;

	@BeforeEach
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(context)
				.apply(SecurityMockMvcConfigurers.springSecurity()) // Configura o MockMvc para usar Spring Security
				.build();
	}

	@Test
	public void should_be_able_to_create_a_job() throws Exception {

		var company = CompanyEntity.builder()
				.description("COMPANY_DESCRIPTION_TEST")
				.email("email@company.com")
				.password("12344567890")
				.username("COMPANY_USERNAME")
				.name("COMPANY_NAME_TEST")
				.build();

		companyRepository.saveAndFlush(company);

		var createJobDTO = CreateJobDTO.builder()
				.benefits("BENEFITS_TEST")
				.description("DESCRIPTION_TEST")
				.level("LEVEL_TEST")
				.build();

		// Realiza uma requisição POST para o endpoint /company/job/ com o CreateJobDTO como corpo.
		mvc.perform(MockMvcRequestBuilders.post("/company/job/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(TestUtils.objectToJson(createJobDTO))
				.header("Authorization", "Bearer " + TestUtils.generateToken(company.getId(), "JAVA_JOBS_@1234#$")))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
