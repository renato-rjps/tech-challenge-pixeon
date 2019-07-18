package br.com.rjps.examapi;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ExamApiApplication.class)
@AutoConfigureMockMvc
public abstract class AbstractSpringIntegrationTest {
	public static final Long MOCK_INSTITUTION_ID = 1l;
	public static final String MOCK_CNPJ = "50.014.935/0001-03";
	public static final String MOCK_CNPJ_INVALID = "123456/0001-03";
	
	@Autowired
	protected MockMvc mvc;

	@Autowired
	protected ObjectMapper objectMapper;

	/**
	 * Convert an object to string
	 * 
	 * @param obj the object to be converted
	 * @return a string with the serialized object
	 */
	public static String asJsonString(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			final String jsonContent = mapper.writeValueAsString(obj);
			return jsonContent;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
