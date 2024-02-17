package com.example.cafehub.repository.specification;

import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Cuisine;
import com.example.cafehub.model.Language;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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

    public Specification<Cafe> hasCuisines(String[] cuisineNames) {
        return (root, query, criteriaBuilder) -> {
            Join<Cafe, Cuisine> cuisinesJoin = root.join("cuisines");
            List<Predicate> predicates = new ArrayList<>();
            for (String cuisine : cuisineNames) {
                predicates.add(criteriaBuilder.equal(cuisinesJoin.get("name"), cuisine));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public Specification<Cafe> hasFastService(boolean fastService) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("fastService"), fastService);
    }

    public Specification<Cafe> hasWifi(boolean wifi) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("wifi"), wifi);
    }

    public Specification<Cafe> hasBusinessLunch(boolean businessLunch) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("businessLunch"), businessLunch);
    }

    public Specification<Cafe> hasFreeWater(boolean freeWater) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("freeWater"), freeWater);
    }

    public Specification<Cafe> hasBoardGames(boolean boardGames) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("boardGames"), boardGames);
    }

    public Specification<Cafe> isBirthday(boolean birthday) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("birthday"), birthday);
    }

    public Specification<Cafe> isBusinessMeeting(boolean businessMeeting) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("businessMeeting"), businessMeeting);
    }

    public Specification<Cafe> isChildHoliday(boolean childHoliday) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("childHoliday"), childHoliday);
    }

    public Specification<Cafe> isRomantic(boolean romantic) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("romantic"), romantic);
    }

    public Specification<Cafe> isThematicEvent(boolean thematicEvent) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("thematicEvent"), thematicEvent);
    }

    public Specification<Cafe> isFamilyHoliday(boolean familyHoliday) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("familyHoliday"), familyHoliday);
    }

    public Specification<Cafe> hasParking(boolean parking) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("parking"), parking);
    }

    public Specification<Cafe> hasTerrace(boolean terrace) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("terrace"), terrace);
    }

    public Specification<Cafe> isOpenNow() {
        return (root, query, criteriaBuilder) -> {
            DayOfWeek currentDayOfWeek = LocalDate.now().getDayOfWeek();
            LocalTime currentTime = LocalTime.now();

            Predicate openDuringWeekdays = criteriaBuilder.and(
                    criteriaBuilder.equal(criteriaBuilder.function("dayofweek", Integer.class,
                            root.get("openFromWeekdays")), currentDayOfWeek.getValue()),
                    criteriaBuilder.lessThanOrEqualTo(root.get("openFromWeekdays"), currentTime),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("closeAtWeekdays"), currentTime)
            );

            Predicate openDuringWeekends = criteriaBuilder.and(
                    criteriaBuilder.equal(criteriaBuilder.function("dayofweek", Integer.class,
                            criteriaBuilder.literal(1)), currentDayOfWeek.getValue()),
                    criteriaBuilder.lessThanOrEqualTo(root.get("openFromWeekends"), currentTime),
                    criteriaBuilder.greaterThanOrEqualTo(root.get("closeAtWeekends"), currentTime)
            );

            return criteriaBuilder.or(openDuringWeekdays, openDuringWeekends);
        };
    }
}
