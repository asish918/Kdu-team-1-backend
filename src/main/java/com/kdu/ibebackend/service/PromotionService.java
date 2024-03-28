package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.constants.ValidationConstants;
import com.kdu.ibebackend.dto.graphql.ListPromotions;
import com.kdu.ibebackend.dto.mappers.PromoCodeMapper;
import com.kdu.ibebackend.dto.response.PromoCodeDTO;
import com.kdu.ibebackend.entities.PromoCode;
import com.kdu.ibebackend.exceptions.custom.InvalidPromoException;
import com.kdu.ibebackend.models.PromotionType;
import com.kdu.ibebackend.repository.PromoCodeRepository;
import com.kdu.ibebackend.utils.PromoUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for fetching all promotions and caching them to optimise API Latency
 */
@Service
@Slf4j
public class PromotionService {
    private GraphQLService graphQLService;
    private PromoCodeRepository promoCodeRepository;

    @Autowired
    public PromotionService(GraphQLService graphQLService, PromoCodeRepository promoCodeRepository) {
        this.graphQLService = graphQLService;
        this.promoCodeRepository = promoCodeRepository;
    }

    @Cacheable("promotions")
    public List<PromotionType> fetchPromotions() {
        ListPromotions data = graphQLService.executePostRequest(GraphQLQueries.promotionQuery, ListPromotions.class).getBody();
        return data.getRes().getPromotionTypeList();
    }

    public ResponseEntity<PromoCodeDTO> validatePromoCode(String promoCode, Integer roomTypeId) throws InvalidPromoException {
        Optional<PromoCode> promo = promoCodeRepository.findByPromoCodeEquals(promoCode);

        if(promo.isEmpty()) throw new InvalidPromoException(ValidationConstants.INVALID_PROMO);
        if(!PromoUtils.promoCodeValidator(promoCode, roomTypeId, promo.get())) throw new InvalidPromoException(ValidationConstants.INVALID_PROMO);

        PromoCodeDTO promoCodeDTO = PromoCodeMapper.entityToDTO(promo.get());

        return new ResponseEntity<>(promoCodeDTO, HttpStatus.OK);
    }

    @CacheEvict("promotions")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
