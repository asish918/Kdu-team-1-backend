package com.kdu.ibebackend;

import com.kdu.ibebackend.constants.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@Slf4j
class IbeBackendApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private WebTestClient webTestClient;

	@Autowired
	private Environment env;

	public static MockWebServer mockBackEnd;

	@BeforeAll
	static void setUp() throws IOException {
		mockBackEnd = new MockWebServer();
		mockBackEnd.start();
	}

	@AfterAll
	static void tearDown() throws IOException {
		mockBackEnd.shutdown();
	}

	@Test
	void testHealthEndpoint() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/test")
						.header("X-Api-Key", AuthConstants.AUTH_TOKEN)) // Include X-Api-Key header
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().string("Hey there!! The server works great 👍"));
	}

	@Test
	void fetchConfig() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/config?tenantId=1")
						.contentType(MediaType.APPLICATION_JSON)
						.header("X-Api-Key", AuthConstants.AUTH_TOKEN)) // Include X-Api-Key header
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.tenantId").value(1));
	}

	@Test
	void fetchProperties() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landingpage/properties")
						.header("X-Api-Key", AuthConstants.AUTH_TOKEN)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.data.listProperties[0].property_name").value("Team 1 Hotel"));
	}

	@Test
	void fetchMinRates() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/landingpage/minrates")
						.contentType(MediaType.APPLICATION_JSON)
						.header("X-Api-Key", AuthConstants.AUTH_TOKEN)) // Include X-Api-Key header
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].date").value("2024-03-01"))
				.andExpect(jsonPath("$[0].price").value(50.0));
	}
}
