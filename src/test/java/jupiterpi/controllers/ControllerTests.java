package jupiterpi.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.io.IOException;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jupiterpi.banking.config.WebAppContextConfig;

import jupiterpi.JupiterpiApplicationTests; 

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ( JupiterpiApplicationTests.class ))
@ContextConfiguration(classes = { WebAppContextConfig.class })
@AutoConfigureMockMvc
public class ControllerTests {
	String PATH;
    public ControllerTests(String base_path) {
    	PATH = base_path;
    }
	
	@Inject
	WebApplicationContext context;

	protected MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	protected MockHttpServletRequestBuilder buildGetRequest(String path) throws Exception {
		return get(PATH + path);
	}

	protected MockHttpServletRequestBuilder buildPostRequest(String path, Object entity) throws Exception {
		return post(PATH + path).content(toJson(entity)).contentType(APPLICATION_JSON_UTF8);
	}

	protected String toJson(Object object) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(object);
	}
    protected <T> T convertJsonContent(MockHttpServletResponse response, Class<T> clazz) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String contentString = response.getContentAsString();
        return objectMapper.readValue(contentString, clazz);
    }
    protected <T> T convertJsonContentList(MockHttpServletResponse response, final TypeReference<T> type) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String contentString = response.getContentAsString();
        return objectMapper.readValue(contentString, type);
    }
}
