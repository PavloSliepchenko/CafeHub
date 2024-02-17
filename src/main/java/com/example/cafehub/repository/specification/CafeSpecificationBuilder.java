package com.example.cafehub.repository.specification;

import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.model.Cafe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CafeSpecificationBuilder {
    private final CafeSpecifications cafeSpecifications;

    public Specification<Cafe> build(CafeSearchParametersDto searchParametersDto) {
        Specification<Cafe> specification = null;
        if (searchParametersDto.languages() != null
                && searchParametersDto.languages().length > 0) {
            specification = Specification.where(
                    cafeSpecifications.hasLanguages(searchParametersDto.languages()));
        }

        if (searchParametersDto.averageBill() != null) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasAverageBillLessThanOrEqual(
                    searchParametersDto.averageBill()))
                    : specification.and(cafeSpecifications.hasAverageBillLessThanOrEqual(
                    searchParametersDto.averageBill()));
        }

        if (searchParametersDto.vegan()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isVegan(searchParametersDto.vegan()))
                    : specification.and(cafeSpecifications.isVegan(searchParametersDto.vegan()));
        }

        if (searchParametersDto.coworking()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isCoworking(
                    searchParametersDto.coworking()))
                    : specification.and(cafeSpecifications.isCoworking(
                    searchParametersDto.coworking()));
        }

        if (searchParametersDto.petFriendly()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isPetFriendly(
                    searchParametersDto.petFriendly()))
                    : specification.and(cafeSpecifications.isPetFriendly(
                    searchParametersDto.petFriendly()));
        }

        if (searchParametersDto.cuisines() != null
                && searchParametersDto.cuisines().length > 0) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasCuisines(
                    searchParametersDto.cuisines()))
                    : specification.and(cafeSpecifications.hasCuisines(
                    searchParametersDto.cuisines()));
        }

        if (searchParametersDto.fastService()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasFastService(
                    searchParametersDto.fastService()))
                    : specification.and(cafeSpecifications.hasFastService(
                    searchParametersDto.fastService()));
        }

        if (searchParametersDto.wifi()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasWifi(
                    searchParametersDto.wifi()))
                    : specification.and(cafeSpecifications.hasWifi(
                    searchParametersDto.wifi()));
        }

        if (searchParametersDto.businessLunch()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasBusinessLunch(
                    searchParametersDto.businessLunch()))
                    : specification.and(cafeSpecifications.hasBusinessLunch(
                    searchParametersDto.businessLunch()));
        }

        if (searchParametersDto.freeWater()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasFreeWater(
                    searchParametersDto.freeWater()))
                    : specification.and(cafeSpecifications.hasFreeWater(
                    searchParametersDto.freeWater()));
        }

        if (searchParametersDto.boardGames()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasBoardGames(
                    searchParametersDto.boardGames()))
                    : specification.and(cafeSpecifications.hasBoardGames(
                    searchParametersDto.boardGames()));
        }

        if (searchParametersDto.birthday()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isBirthday(
                    searchParametersDto.birthday()))
                    : specification.and(cafeSpecifications.isBirthday(
                    searchParametersDto.birthday()));
        }

        if (searchParametersDto.businessMeeting()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isBusinessMeeting(
                    searchParametersDto.businessMeeting()))
                    : specification.and(cafeSpecifications.isBusinessMeeting(
                    searchParametersDto.businessMeeting()));
        }

        if (searchParametersDto.childHoliday()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isChildHoliday(
                    searchParametersDto.childHoliday()))
                    : specification.and(cafeSpecifications.isChildHoliday(
                    searchParametersDto.childHoliday()));
        }

        if (searchParametersDto.romantic()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isRomantic(
                    searchParametersDto.romantic()))
                    : specification.and(cafeSpecifications.isRomantic(
                    searchParametersDto.romantic()));
        }

        if (searchParametersDto.thematicEvent()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isThematicEvent(
                    searchParametersDto.thematicEvent()))
                    : specification.and(cafeSpecifications.isThematicEvent(
                    searchParametersDto.thematicEvent()));
        }

        if (searchParametersDto.familyHoliday()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isFamilyHoliday(
                    searchParametersDto.familyHoliday()))
                    : specification.and(cafeSpecifications.isFamilyHoliday(
                    searchParametersDto.familyHoliday()));
        }

        if (searchParametersDto.parking()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasParking(
                    searchParametersDto.parking()))
                    : specification.and(cafeSpecifications.hasParking(
                    searchParametersDto.parking()));
        }

        if (searchParametersDto.terrace()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasTerrace(
                    searchParametersDto.terrace()))
                    : specification.and(cafeSpecifications.hasTerrace(
                    searchParametersDto.terrace()));
        }

        if (searchParametersDto.openNow()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isOpenNow())
                    : specification.and(cafeSpecifications.isOpenNow());
        }
        return specification == null ? Specification.where(null) : specification;
    }
}
