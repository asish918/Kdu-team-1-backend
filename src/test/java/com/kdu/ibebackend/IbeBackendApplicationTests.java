package com.kdu.ibebackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.dto.GraphQLResponse;
import com.kdu.ibebackend.service.GraphQLService;
import okhttp3.mockwebserver.MockResponse;
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
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Map;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class IbeBackendApplicationTests {
//	@Autowired
//	private MockMvc mockMvc;
//
//	@Autowired
//	private WebTestClient webTestClient;
//
//	@Autowired
//	private Environment env;
//
//	public static MockWebServer mockBackEnd;
//	private GraphQLService graphQLService;
//
//	@BeforeEach
//	void initialize() {
//		String baseUrl = String.format("http://localhost:%s",
//				mockBackEnd.getPort());
//		this.graphQLService = new GraphQLService(env.getProperty("graphql.url"), env.getProperty("graphql.api.key"));
//	}
//
//
//	@BeforeAll
//	static void setUp() throws IOException {
//		mockBackEnd = new MockWebServer();
//		mockBackEnd.start();
//	}
//
//	@AfterAll
//	static void tearDown() throws IOException {
//		mockBackEnd.shutdown();
//	}
//
//	@Test
//	void testHealthEndpoint() throws Exception {
//		mockMvc.perform(MockMvcRequestBuilders.get("/test"))
//				.andExpect(MockMvcResultMatchers.status().isOk())
//				.andExpect(MockMvcResultMatchers.content().string("Hey there!! The server works great 👍"));
//	}
//
//	@Test
//	void testGraphQL() {
//		String query = "{\n" +
//				"    roomById(id: 1) {\n" +
//				"        room_id\n" +
//				"        name\n" +
//				"        tenant {\n" +
//				"            tenant_id\n" +
//				"            firstName\n" +
//				"            lastName\n" +
//				"        }\n" +
//				"    }\n" +
//				"}";
//
//		webTestClient.post()
//				.uri("/graphql")
//				.contentType(MediaType.APPLICATION_JSON)
//				.body(Mono.just(toJSON(query)), String.class)
//				.exchange()
//				.expectStatus().isOk()
//				.expectBody()
//				.jsonPath("$.data.roomById.name").isEqualTo("Luxury");
//	}
//
//	private static String toJSON(String query) {
//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			return objectMapper.writeValueAsString(Map.of("query", query));
//		} catch (JsonProcessingException e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	@Test
//	void testGraphQLEndpoint() throws Exception {
//		ObjectMapper objectMapper = new ObjectMapper();
//		GraphQLResponse graphQLResponse = new GraphQLResponse();
//		GraphQLResponse.Res res = new GraphQLResponse.Res();
//		res.setCountRooms(1080);
//		graphQLResponse.setRes(res);
//
//		mockBackEnd.enqueue(new MockResponse()
//				.setBody(objectMapper.writeValueAsString(graphQLResponse))
//				.addHeader("Content-Type", "application/json"));
//
//		Mono<GraphQLResponse> graphMono = graphQLService.executePostRequest();
//
//		StepVerifier.create(graphMono)
//				.expectNextMatches(graph -> graph.getRes().getCountRooms() == 1080)
//				.verifyComplete();
//	}
}
