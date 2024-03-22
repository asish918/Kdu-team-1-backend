package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.response.PromotionData;
import com.kdu.ibebackend.models.PromotionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotionService {
    private GraphQLService graphQLService;

    @Autowired
    public PromotionService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    @Cacheable("promotions")
    public List<PromotionType> fetchPromotions() {
        PromotionData data = graphQLService.executePostRequest(GraphQLQueries.promotionQuery, PromotionData.class).getBody();
        return data.getRes().getPromotionTypeList();
    }

    @CacheEvict("promotions")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
