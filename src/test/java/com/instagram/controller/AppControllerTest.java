package com.instagram.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.instagram.InstaHomeApplication;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InstaHomeApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DisplayName("AppControllerTest")
public class AppControllerTest {
	
	@LocalServerPort private int port;
	
	@Autowired private WebApplicationContext context;
	
	private MockMvc mockMvc;
	
	@Before
	public void before() {
		mockMvc = MockMvcBuilders
					.webAppContextSetup(context)
					.apply(springSecurity())
					.build();
	}
	
	@Test
	@DisplayName("Test with Empty Request Body.")
	public void testWithEmptyBody() throws Exception {
		mockMvc
			.perform(post("/login")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Test with Invalid Email or password.")
	public void testWithInvalidCredential() throws Exception {
		String loginRequestJSON = "{\"email\":\"admin\",\"password\":\"Pass@123\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/login")
				.accept(MediaType.APPLICATION_JSON).content(loginRequestJSON)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Test with Valid Credentials.")
	public void testWithValidCredentials() throws Exception {
		String loginRequestJSON = "{\"email\":\"faeemmansuri0786@gmail.com\",\"password\":\"Pass@1234\"}";

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/login")
				.accept(MediaType.APPLICATION_JSON).content(loginRequestJSON)
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(requestBuilder).andExpect(status().isOk());
	}
	
}