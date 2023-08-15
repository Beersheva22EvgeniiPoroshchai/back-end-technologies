package telran.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.spring.controller.SenderController;
import telran.spring.model.Message;

import telran.spring.service.Sender;

@Service
class MockSender implements Sender {

	
	public String send(Message message) {
		// TODO Auto-generated method stub
		return "test";
	}

	
	public String getMessageTypeString() {
		// TODO Auto-generated method stub
		return "test";
	}

	
	public Class<? extends Message> getMessageTypeObject() {
		// TODO Auto-generated method stub
		return Message.class;
	}
}

@WithMockUser(roles = {"USER", "ADMIN"}, username = "admin")
@WebMvcTest({SenderController.class, MockSender.class})  // load beans into the AppContext, just for testing part of the web, all we need, except web server
class SendersControllerTest {

	
@Autowired    // spring finds mockMvc, get reference and inject into the AppContext, imitation requests from server 
MockMvc mockMvc;	//web server


@Autowired
ObjectMapper mapper;
Message message;
String sendUrl = "http://localhost:8080/sender";
String getTypesUrl = sendUrl;
String isTypePathUrl = String.format("%s/type", sendUrl);



@BeforeEach
void setUp() {
	message = new Message();
	message.text = "test";
	message.type = "test";
}


	@Test
	void mockMvcExists() {
		assertNotNull(mockMvc);
		
	}
	
	@Test
	void sendRightFlow() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		assertEquals("test", response);
	}
	
	
	@Test
	@WithMockUser(roles = {"USER"}, username = "admin")
	void sendFlow403() throws Exception {
		String messageJson = mapper.writeValueAsString(message);
		getRequestBase(messageJson).andExpect(status().isForbidden());
	
	}

	private ResultActions getRequestBase(String messageJson) throws Exception {
		return mockMvc.perform(post(sendUrl).contentType(MediaType.APPLICATION_JSON).content(messageJson)).
				andDo(print());
	}
	
	
	@Test
	void sendNotFoundFlow() throws Exception {
		message.type = "abc";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
		assertEquals("abc type not found", response);
	}
	
	
	@Test
	void sendValidationViolationFlow() throws Exception {
		message.type = "123";
		String messageJson = mapper.writeValueAsString(message);
		String response = getRequestBase(messageJson).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		assertTrue(response.contains("mismatches"));
	}
	
	@Test
	void getTypesTest() throws Exception {
		String responseJson = mockMvc.perform(get(getTypesUrl)).andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		String[] typeResponse = mapper.readValue(responseJson, String[].class);
		assertArrayEquals(new String[] {"test"}, typeResponse);
	}
	
	@Test
	void isTypePathExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test"))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean boolResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(boolResponse);
	}
	
	@Test
	void isTypePathNoExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "/test1"))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean boolResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(boolResponse);
	}
	
	
	@Test
	void isTypePathParamsExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test"))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean boolResponse = mapper.readValue(responseJson, boolean.class);
		assertTrue(boolResponse);
	}
	
	@Test
	void isTypePathParamsNoExists() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl + "?type=test1"))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
		Boolean boolResponse = mapper.readValue(responseJson, boolean.class);
		assertFalse(boolResponse);
	}
	
	@Test
	void isTypePathParamsMissing() throws Exception {
		String responseJson = mockMvc.perform(get(isTypePathUrl))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();
		
		assertEquals(responseJson, "isTypeExistsParam.type: must not be empty");
	}
	
	
	
	
	
	
	
	
	
	
	
	

	

}
