package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.graphql.ListPromotions;
import com.kdu.ibebackend.models.PromotionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for fetching all promotions and caching them to optimise API Latency
 */
@Service
public class PromotionService {
    private GraphQLService graphQLService;

    @Autowired
    public PromotionService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @Cacheable("promotions")
    public List<PromotionType> fetchPromotions() {
        ListPromotions data = graphQLService.executePostRequest(GraphQLQueries.promotionQuery, ListPromotions.class).getBody();
        return data.getRes().getPromotionTypeList();
    }

    @CacheEvict("promotions")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
