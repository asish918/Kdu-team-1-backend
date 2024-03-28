package com.kdu.ibebackend.utils;

import com.kdu.ibebackend.constants.PromoType;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.entities.PromoCode;
import com.kdu.ibebackend.models.PromotionType;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Util function to validate Promotions
 */
@Slf4j
public class PromoUtils {
    public static Boolean validDayRange(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        return promotionType.getMinimumDaysOfStay() <= DateUtils.calculateDaysBetween(searchParamDTO.getStartDate(), searchParamDTO.getEndDate());
    }
    public static Boolean checkSeniorCitizen(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        if(Boolean.FALSE.equals(searchParamDTO.getIsSeniorCitizen())) return false;
        return validDayRange(promotionType, searchParamDTO) && Boolean.TRUE.equals(searchParamDTO.getIsSeniorCitizen()) && Integer.parseInt(promotionType.getPromotionId()) == PromoType.SENIOR_CITIZEN.getPromotionId();
    }

    public static Boolean checkKDUMember(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        if(Boolean.FALSE.equals(searchParamDTO.getIsKDUMember())) return false;
        return validDayRange(promotionType, searchParamDTO) && Boolean.TRUE.equals(searchParamDTO.getIsKDUMember()) && Integer.parseInt(promotionType.getPromotionId()) == PromoType.KDU_MEMBER.getPromotionId();
    }

    public static Boolean checkWeekend(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDate startDate = LocalDate.parse(searchParamDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(searchParamDTO.getEndDate(), formatter);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                return validDayRange(promotionType, searchParamDTO) && Integer.parseInt(promotionType.getPromotionId()) == PromoType.WEEKEND.getPromotionId();
            }
        }
        return false;
    }
    public static boolean checkLongWeekend(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        LocalDate startDate = LocalDate.parse(searchParamDTO.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(searchParamDTO.getEndDate(), formatter);

        LocalDate nextSaturday = getNextDayOfWeek(startDate, DayOfWeek.SATURDAY);
        LocalDate nextSunday = getNextDayOfWeek(startDate, DayOfWeek.SUNDAY);

        while (nextSunday.isBefore(endDate)) {
            if (nextSaturday.isAfter(startDate) && nextSaturday.isBefore(endDate) &&
                    nextSunday.isAfter(startDate) && nextSunday.isBefore(endDate)) {
                return validDayRange(promotionType, searchParamDTO) && Integer.parseInt(promotionType.getPromotionId()) == PromoType.LONG_WEEKEND.getPromotionId();
            }
            nextSaturday = getNextDayOfWeek(nextSaturday.plusDays(1), DayOfWeek.SATURDAY);
            nextSunday = getNextDayOfWeek(nextSunday.plusDays(1), DayOfWeek.SUNDAY);
        }

        return false;
    }

    private static LocalDate getNextDayOfWeek(LocalDate date, DayOfWeek dayOfWeek) {
        int daysUntilNext = (dayOfWeek.getValue() - date.getDayOfWeek().getValue() + 7) % 7;
        return date.plusDays(daysUntilNext);
    }


    public static boolean checkMilitaryPersonal(PromotionType promotionType, SearchParamDTO searchParamDTO) {
        if(Boolean.FALSE.equals(searchParamDTO.getIsMilitary())) return false;
        return validDayRange(promotionType, searchParamDTO) && Boolean.TRUE.equals(searchParamDTO.getIsMilitary()) && Integer.parseInt(promotionType.getPromotionId()) == PromoType.MILITARY.getPromotionId();
    }

    public static boolean promoCodeValidator(String promo, Integer roomTypeId, PromoCode promoCode) {
        log.info(roomTypeId.toString());
        if(!Objects.equals(roomTypeId, promoCode.getRoomTypeId())) return false;
        if(!Objects.equals(promo, promoCode.getPromoCode())) return false;

        LocalDate today = LocalDate.now();
        LocalDate startDate = promoCode.getStartDate().toLocalDate();
        LocalDate endDate = promoCode.getEndDate().toLocalDate();

        return today.isAfter(startDate) || today.isBefore(endDate);
    }
}
