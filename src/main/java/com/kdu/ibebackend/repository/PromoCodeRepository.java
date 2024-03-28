package com.kdu.ibebackend.repository;

import com.kdu.ibebackend.entities.PromoCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PromoCodeRepository extends JpaRepository<PromoCode, Integer> {
    Optional<PromoCode> findByPromoCodeEquals(String promoCode);
}