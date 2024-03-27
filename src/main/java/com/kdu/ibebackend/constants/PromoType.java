package com.kdu.ibebackend.constants;

/**
 * Enum for Promotion types coming in from GraphQL Backend
 */
public enum PromoType {
    SENIOR_CITIZEN(1),
    KDU_MEMBER(2),
    LONG_WEEKEND(3),
    WEEKEND(6),
    UPFRONT(5),
    MILITARY(4);

    private final Integer promotionId;

    PromoType(Integer promotionId) {
        this.promotionId = promotionId;
    }

    public Integer getPromotionId() {
        return promotionId;
    }
}
