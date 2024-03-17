package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.FetchProperties;
import com.kdu.ibebackend.dto.MinRates;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Controller for all landing page endpoints
 */
@Controller
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/landingpage")
public class LandingPageController {
    private final PropertyService propertyService;
    private final GraphQLService graphQLService;

    @Autowired
    LandingPageController(PropertyService propertyService, GraphQLService graphQLService) {
        this.propertyService = propertyService;
        this.graphQLService = graphQLService;
    }


    @GetMapping("/minrates")
    public ResponseEntity<List<MinRates>> getMinimumNightRate() {
        try {
            Map<LocalDate, Double> minNightRates = propertyService.getMinimumNightRate();
            List<MinRates> res = MinRates.convertMapToList(minNightRates);
            MinRates.sortListByDate(res);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/properties")
    public ResponseEntity<FetchProperties> getProperties() {
        String query = GraphQLQueries.fetchProperties;
        return graphQLService.executePostRequest(query, FetchProperties.class);
    }
}
