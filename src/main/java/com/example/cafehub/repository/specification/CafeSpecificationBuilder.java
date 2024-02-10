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

        if (searchParametersDto.childZone()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.hasChildZone(
                    searchParametersDto.childZone()))
                    : specification.and(cafeSpecifications.hasChildZone(
                    searchParametersDto.childZone()));
        }

        if (searchParametersDto.vegan()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isVegan(searchParametersDto.vegan()))
                    : specification.and(cafeSpecifications.isVegan(searchParametersDto.vegan()));
        }

        if (searchParametersDto.smokingFriendly()) {
            specification = specification == null
                    ? Specification.where(cafeSpecifications.isSmokingFriendly(
                    searchParametersDto.smokingFriendly()))
                    : specification.and(cafeSpecifications.isSmokingFriendly(
                    searchParametersDto.smokingFriendly()));
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
        return specification == null ? Specification.where(null) : specification;
    }
}
