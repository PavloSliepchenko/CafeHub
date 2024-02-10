package com.example.cafehub.repository.specification;

import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Language;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CafeSpecifications {
    public Specification<Cafe> hasLanguages(String[] languages) {
        return (root, query, criteriaBuilder) -> {
            Join<Cafe, Language> languagesJoin = root.join("languages");
            List<Predicate> predicates = new ArrayList<>();
            for (String language : languages) {
                predicates.add(criteriaBuilder.equal(languagesJoin.get("name"), language));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Cafe> isCoworking(boolean coworking) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("coworking"), coworking);
    }

    public Specification<Cafe> isVegan(boolean vegan) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("vegan"), vegan);
    }

    public Specification<Cafe> isPetFriendly(boolean petFriendly) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("petFriendly"), petFriendly);
    }

    public Specification<Cafe> hasAverageBillLessThanOrEqual(BigDecimal averageBill) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get("averageBill"), averageBill);
    }

    public Specification<Cafe> hasChildZone(boolean childZone) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("childZone"), childZone);
    }

    public Specification<Cafe> isSmokingFriendly(boolean smokingFriendly) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("smokingFriendly"), smokingFriendly);
    }
}
