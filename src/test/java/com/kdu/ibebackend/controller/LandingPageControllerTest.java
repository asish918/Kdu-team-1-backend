package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.AuthConstants;
import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.graphql.ListProperties;
import com.kdu.ibebackend.models.Property;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.PropertyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LandingPageController.class)
@ComponentScan(value = "com.kdu.ibebackend.config")
@ActiveProfiles("test")
public class LandingPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PropertyService propertyService;

    @MockBean
    private GraphQLService graphQLService;

    @Test
    public void getMinimumNightRate_ReturnsMinRatesList() throws Exception {
        Map<LocalDate, Double> minNightRates = new HashMap<>();
        minNightRates.put(LocalDate.of(2022, 3, 1), 50.0);
        minNightRates.put(LocalDate.of(2022, 3, 2), 50.0);
        given(propertyService.getMinimumNightRate()).willReturn(minNightRates);

        mockMvc.perform(get("/api/landingpage/minrates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Api-Key", AuthConstants.AUTH_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].date").value("2022-03-01"))
                .andExpect(jsonPath("$[0].price").value(50.0))
                .andExpect(jsonPath("$[1].date").value("2022-03-02"))
                .andExpect(jsonPath("$[1].price").value(50.0));
    }

    @Test
    public void getProperties_ReturnsFetchProperties() throws Exception {
        List<Property> mockProperties = new ArrayList<>();
        mockProperties.add(new Property("Property 1", 1));
        mockProperties.add(new Property("Property 2", 2));

        ListProperties fetchProperties = new ListProperties();
        ListProperties.Res propRes = new ListProperties.Res();
        propRes.setProperties(mockProperties);
        fetchProperties.setRes(propRes);
        ResponseEntity<ListProperties> res = new ResponseEntity<>(fetchProperties, HttpStatus.OK);

        given(graphQLService.executePostRequest(GraphQLQueries.fetchProperties, ListProperties.class)).willReturn(res);

        mockMvc.perform(get("/api/landingpage/properties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Api-Key", AuthConstants.AUTH_TOKEN))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.listProperties[0].property_name").value("Property 1"))
                .andExpect(jsonPath("$.data.listProperties[1].property_name").value("Property 2"))
                .andExpect(jsonPath("$.data.listProperties[0].property_id").value(1))
                .andExpect(jsonPath("$.data.listProperties[1].property_id").value(2));
    }
}
