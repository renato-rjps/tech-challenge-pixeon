package br.com.rjps.examapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.github.javafaker.Faker;

import br.com.rjps.examapi.model.HealthcareInstitution;

@SqlGroup({
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD,scripts = "classpath:clear.sql") 
})
public class HealthcareInstitutionIntegrationTests extends AbstractSpringIntegrationTest{
	
	public static final String BASE_URL = "/healthcareInstitutions";


	@Test
	public void deveConsularUmaInstituicaoPeloId() throws Exception {
	    mvc.perform(get(BASE_URL + "/1"))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.enabled").value(true))
	      .andExpect(jsonPath("$.id").value(1))
	      .andExpect(jsonPath("$.createdDate").exists())
	      .andExpect(jsonPath("$.modifiedDate").exists());
	}
		
	@Test
	public void deveRetornarStatus405AoTentarDeletarInstituicao() throws Exception {
	    mvc.perform(delete(BASE_URL + "/1"))
	      .andExpect(status().isMethodNotAllowed());
	}
	
	@Test
	public void deveCriarUmaInstituicao() throws Exception {
		Faker faker = new Faker();	 
		HealthcareInstitution institution = new HealthcareInstitution();
		institution.setName(faker.company().name());
		institution.setCnpj(MOCK_CNPJ);
		
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isCreated());	
	}
	
	
	@Test
	public void naoDeveCriarUmaInstituicaoComCNPJExistente() throws Exception {
		Faker faker = new Faker();	 
		HealthcareInstitution institution = new HealthcareInstitution();
		institution.setName(faker.company().name());
		institution.setCnpj(MOCK_CNPJ);
		
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isCreated());	
	    
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isBadRequest());	
	}
	
	
	@Test
	public void naoDeveCriarUmaInstituicaoComCNPJInvalido() throws Exception {
		Faker faker = new Faker();	 
		HealthcareInstitution institution = new HealthcareInstitution();
		institution.setName(faker.company().name());
		institution.setCnpj(MOCK_CNPJ_INVALID);
		
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isBadRequest())
	      .andDo(MockMvcResultHandlers.print())
	      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(jsonPath("$.errors[0]").value("cnpj: invalid Brazilian corporate taxpayer registry number (CNPJ)"))
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Invalid Request"));	    
	}
	
	
	@Test
	public void naoDeveCriarUmaInstituicaoQuandoNaoInformadoNome_E_Cnpj() throws Exception {
		HealthcareInstitution institution = new HealthcareInstitution();
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isBadRequest())
	      .andDo(MockMvcResultHandlers.print())
	      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Invalid Request"));	    
	}
	
	@Test
	public void umaInstiuicaoDevePossuir20MoedasAoSerCriada() throws Exception {
		Faker faker = new Faker();	 
		HealthcareInstitution institution = new HealthcareInstitution();
		institution.setName(faker.company().name());
		institution.setCnpj(MOCK_CNPJ);
		
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(institution)))
	      .andExpect(status().isCreated());
	    
	    mvc.perform(get(BASE_URL+ "/2")
	    		.content(asJsonString(institution)))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.coins").value(20))
	      .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void deveRetornarListaDeInstituicoes() throws Exception {
	    mvc.perform(get(BASE_URL))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$._embedded.healthcareInstitutions[0].name").value("SOS Cardio"))
	      .andExpect(jsonPath("$._embedded.healthcareInstitutions.length()").value(1))
	      .andDo(MockMvcResultHandlers.print());
	}

}
