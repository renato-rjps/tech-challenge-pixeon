package br.com.rjps.examapi;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.github.javafaker.Faker;

import br.com.rjps.examapi.model.Exam;
import br.com.rjps.examapi.model.Gender;
import br.com.rjps.examapi.model.HealthcareInstitution;
import br.com.rjps.examapi.repository.ExamRepository;
import br.com.rjps.examapi.repository.HealthcareInstitutionRepository;

@SqlGroup({
	@Sql(executionPhase = ExecutionPhase.AFTER_TEST_METHOD,scripts = "classpath:clear.sql") 
})
public class ExamIntegrationTests extends AbstractSpringIntegrationTest{
	
	public static final String BASE_URL = "/exams";

	@Autowired
	private ExamRepository examRepository;
	
	@Autowired
	private HealthcareInstitutionRepository institutionRepository;
	
	@Test
	public void deveCriarUmExame() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated())
	      .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void deveAtualizarExame() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
	
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated());
	    
	    mvc.perform(get(BASE_URL + "/1"))
	      .andExpect(status().isOk());
	    
	    String name = "Updated Name";
	    String gender = "OTHERS";
	    
	    mvc.perform(patch(BASE_URL + "/1")
	    		.content("{ \"patientName\": \"" + name + "\", \"patientGender\": \""+ gender + "\"}"))
	      .andExpect(status().isNoContent());
	    
	    mvc.perform(get(BASE_URL + "/1"))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.patientName").value(name))
	      .andExpect(jsonPath("$.patientGender").value(gender));
	}
	
	@Test
	public void deveExcluirExame() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
	
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated());
	    
	    mvc.perform(get(BASE_URL + "/1"))
	      .andExpect(status().isOk());
	    
	    mvc.perform(delete(BASE_URL + "/1"))
	      .andExpect(status().isNoContent());
	    
	    mvc.perform(get(BASE_URL + "/1"))
	      .andExpect(status().isNotFound());
	}
	
	@Test
	public void naoDeveCriarUmExameQuandoInformadoUmSexoInvalido() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		body = body.replace("MALE", "INVALID");
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isBadRequest())
	      .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void naoDeveCriarUmExameQuandoIdadeForMaiorQue150() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		body = body.replace("\"patientAge\":null", "\"patientAge\":151");

	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isBadRequest())
	      .andExpect(jsonPath("$.errors[0]").value("patientAge: must be less than or equal to 150"))
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Invalid Request"))
	      .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void naoDeveCriarExameQuandoInstituicaoNaoPossuirMaisMoedas() throws Exception {
		
		Optional<HealthcareInstitution> institution = institutionRepository.findById(MOCK_INSTITUTION_ID);
		
		institution.get().setCoins(0);
		
		institutionRepository.save(institution.get());
		
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		
		mvc.perform(post(BASE_URL)
				.content(body))
		  .andExpect(status().isBadRequest())
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Sorry, The institution has no more coins to perform this operation!"))
		  .andDo(MockMvcResultHandlers.print());
	}
	
	
	@Test
	public void naoDeveLerExameQuandoNaoPossuirMaisMoedas_E_ExameNaoFoiLidoAnteriormente() throws Exception {
		
		// Atualiza quantidade de moedas
		Optional<HealthcareInstitution> institution = institutionRepository.findById(MOCK_INSTITUTION_ID);
		institution.get().setCoins(1);
		institutionRepository.save(institution.get());
		
		// Cria um exame para gastar a ultima moeda
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated());
	    
	    // Tenta ler o exame
	    mvc.perform(get(BASE_URL + "/1")
	    		.content(body))
	    .andExpect(status().isBadRequest())
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Sorry, The institution has no more coins to perform this operation!"));
	}
	
	@Test
	public void deveLerExameQuandoNaoPossuirMaisMoedas_Mas_ExameFoiLidoAnteriormente() throws Exception {
		
		// Atualiza quantidade de moedas
		Optional<HealthcareInstitution> institution = institutionRepository.findById(MOCK_INSTITUTION_ID);
		institution.get().setCoins(1);
		institutionRepository.save(institution.get());
		
		// Cria um exame para gastar a ultima moeda
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated());
	    
    	// Atualiza exame para lido
 		Optional<Exam> exam = examRepository.findById(1l);
 		exam.get().setRead(true);
 		examRepository.save(exam.get());
	    
	    // Tenta ler o exame
	    mvc.perform(get(BASE_URL + "/1")
	    		.content(body))
	    .andExpect(status().isOk());
	}
	
	@Test
	public void umaInstituicaoNaoDeveLerUmExameAtribuidoAOutraInstituicao() throws Exception {
		
		// Cria insituicao
		HealthcareInstitution institution = new HealthcareInstitution();
		institution.setCnpj(MOCK_CNPJ);
		institution.setName("Teste");
		institution.setCoins(20);
		HealthcareInstitution createdInstitution = institutionRepository.save(institution);
		 	    
		// Cria exame para a instituicao criada
		String body = buildExamJson(createdInstitution.getId());
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated());
	    	        	    
	    // Le exame da instituicao criada
	    String path1 = new StringBuilder(HealthcareInstitutionIntegrationTests.BASE_URL)
	    		.append("/")
	    		.append(createdInstitution.getId())
	    		.append(BASE_URL)
	    		.append("/")
	    		.append(1).toString();
	    
	    mvc.perform(get(path1))
	    .andExpect(status().isOk());
	    
	    
	 // Le exame da instituicao criada a partir da instituicao mock
	    String path2 = new StringBuilder(HealthcareInstitutionIntegrationTests.BASE_URL)
	    		.append("/")
	    		.append(MOCK_INSTITUTION_ID)
	    		.append(BASE_URL)
	    		.append("/")
	    		.append(1).toString();
	    
	    mvc.perform(get(path2))
	    .andExpect(status().isNotFound())
	    .andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	public void aoConsultarUmExameEleDeveSerMarcadoComoLido() throws Exception {
		String body = buildExamJson(MOCK_INSTITUTION_ID);
		
	    mvc.perform(post(BASE_URL)
	    		.content(body))
	      .andExpect(status().isCreated())
	      .andDo(MockMvcResultHandlers.print());
	    
	    
	    mvc.perform(get(BASE_URL + "/1")
	    		.content(body))
	      .andExpect(status().isOk())
	      .andExpect(jsonPath("$.read").value(true))
	      .andDo(MockMvcResultHandlers.print());
	}

	private String buildExamJson(Long institutionId) {
		Exam exam = buildFakeExam();
		
		String asJsonString = asJsonString(exam);
		
		asJsonString = asJsonString.replace("\"healthcareInstitution\":null", "\"healthcareInstitution\":\"healthcareInstitution/"+institutionId+"\"");
		return asJsonString;
	}

	private Exam buildFakeExam() {
		Faker faker = new Faker();	 
		Exam exam = new Exam();
		exam.setPatientName(faker.name().fullName());
		exam.setPhysicianName(faker.name().fullName());
		exam.setPhysicianCrm(faker.number().randomNumber(5, true));
		exam.setProcedureName(faker.name().fullName());
		exam.setPatientGender(Gender.MALE);
		return exam;
	}
	
	@Test
	public void naoDeveCriarExameQuandoNaoInformadoCamposObrigatorios() throws Exception {
		Exam exam = new Exam();
		
	    mvc.perform(post(BASE_URL)
	    		.content(asJsonString(exam)))
	      .andExpect(status().isBadRequest())
	      .andExpect(jsonPath("$.errors.length()").value(5))
	      .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
	      .andExpect(jsonPath("$.message").value("Invalid Request"))
	      .andDo(MockMvcResultHandlers.print());
	}
	

}
