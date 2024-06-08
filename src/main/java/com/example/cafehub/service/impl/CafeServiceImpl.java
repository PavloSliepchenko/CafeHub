package com.example.cafehub.service.impl;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.dto.comment.CommentResponseDto;
import com.example.cafehub.exception.EntityAlreadyExistsException;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Cuisine;
import com.example.cafehub.model.Language;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.CuisineRepository;
import com.example.cafehub.repository.LanguageRepository;
import com.example.cafehub.repository.specification.CafeSpecificationBuilder;
import com.example.cafehub.service.CafeService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeSpecificationBuilder specificationBuilder;
    private final LanguageRepository languageRepository;
    private final CuisineRepository cuisineRepository;
    private final CafeRepository cafeRepository;
    private final CafeMapper cafeMapper;

    @Override
    public List<CafeResponseDto> getByName(String name, String city) {
        List<CafeResponseDto> cafes = cafeRepository.findByName(name.trim(), city).stream()
                .map(cafeMapper::toDto)
                .toList();
        orderComments(cafes);
        return cafes;
    }

    @Override
    public List<CafeResponseDto> getAllCafes(Pageable pageable) {
        List<CafeResponseDto> cafes = cafeRepository.findAll(pageable).stream()
                .map(cafeMapper::toDto)
                .toList();
        orderComments(cafes);
        return cafes;
    }

    @Override
    public List<CafeResponseDto> getAllCafesInCity(String cityName, Pageable pageable) {
        List<CafeResponseDto> cafes = cafeRepository.findByCity(cityName, pageable).stream()
                .map(cafeMapper::toDto)
                .toList();
        orderComments(cafes);
        return cafes;
    }

    @Override
    public CafeResponseDto getCafeById(Long cafeId) {
        CafeResponseDto cafe = cafeMapper.toDto(findCafeById(cafeId));
        orderComments(List.of(cafe));
        return cafe;
    }

    @Override
    public CafeResponseDto addCafe(CreateCafeDto createCafeDto) {
        if (cafeRepository.findByNameAndAddressAndCity(
                        createCafeDto.getName(),
                        createCafeDto.getAddress(),
                        createCafeDto.getCity())
                .isPresent()) {
            throw new EntityAlreadyExistsException("This cafe was added before");
        }
        Cafe cafe = cafeMapper.toModel(createCafeDto);
        cafe.setScore(BigDecimal.ZERO);
        cafe.setTotalScore(BigDecimal.ZERO);
        cafe.setNumberOfUsersVoted(BigDecimal.ZERO);
        return cafeMapper.toDto(cafeRepository.save(cafe));
    }

    @Override
    public List<CafeResponseDto> cafeSearch(CafeSearchParametersDto searchParametersDto) {
        Specification<Cafe> cafeSpecification = specificationBuilder.build(searchParametersDto);
        List<CafeResponseDto> cafes = cafeRepository.findAll(cafeSpecification).stream()
                .map(cafeMapper::toDto)
                .toList();
        orderComments(cafes);
        return cafes;
    }

    @Override
    public void deleteCafe(Long cafeId) {
        cafeRepository.delete(findCafeById(cafeId));
    }

    @Override
    public CafeResponseDto updateCafeInfo(Long cafeId, CreateCafeDto createCafeDto) {
        Cafe cafe = findCafeById(cafeId);
        if (createCafeDto.getAddress() != null) {
            cafe.setAddress(createCafeDto.getAddress());
        }
        if (createCafeDto.getCity() != null) {
            cafe.setCity(createCafeDto.getCity());
        }
        if (createCafeDto.getName() != null) {
            cafe.setName(createCafeDto.getName());
        }
        if (createCafeDto.getDescription() != null) {
            cafe.setDescription(createCafeDto.getDescription());
        }
        if (createCafeDto.getPhoneNumber() != null) {
            cafe.setPhoneNumber(createCafeDto.getPhoneNumber());
        }
        if (createCafeDto.getAverageBill() != null) {
            cafe.setAverageBill(createCafeDto.getAverageBill());
        }
        if (createCafeDto.getCloseAtWeekdays() != null) {
            cafe.setCloseAtWeekdays(createCafeDto.getCloseAtWeekdays());
        }
        if (createCafeDto.getCloseAtWeekends() != null) {
            cafe.setCloseAtWeekends(createCafeDto.getCloseAtWeekends());
        }
        if (createCafeDto.getOpenFromWeekdays() != null) {
            cafe.setOpenFromWeekdays(createCafeDto.getOpenFromWeekdays());
        }
        if (createCafeDto.getOpenFromWeekends() != null) {
            cafe.setOpenFromWeekends(createCafeDto.getOpenFromWeekends());
        }
        if (createCafeDto.getUrlOfImage() != null) {
            cafe.setUrlOfImage(createCafeDto.getUrlOfImage());
        }
        if (createCafeDto.getUrlToGoogleMaps() != null) {
            cafe.setUrlToGoogleMaps(createCafeDto.getUrlToGoogleMaps());
        }
        if (createCafeDto.getWebSite() != null) {
            cafe.setWebSite(createCafeDto.getWebSite());
        }
        if (createCafeDto.getCuisineNames() != null
                && !createCafeDto.getCuisineNames().isEmpty()) {
            List<Cuisine> allCuisines = cuisineRepository.findAll();
            List<Cuisine> cafesCuisines = new ArrayList<>();
            for (Cuisine cuisine : allCuisines) {
                if (createCafeDto.getCuisineNames().contains(cuisine.getName())) {
                    cafesCuisines.add(cuisine);
                }
            }
            cafe.setCuisines(cafesCuisines);
        }
        if (createCafeDto.getImages() != null && !createCafeDto.getImages().isEmpty()) {
            cafe.setImages(createCafeDto.getImages());
        }
        if (createCafeDto.getLanguageNames() != null
                && !createCafeDto.getLanguageNames().isEmpty()) {
            List<Language> allLanguages = languageRepository.findAll();
            List<Language> cafesLanguages = new ArrayList<>();
            for (Language language : allLanguages) {
                if (createCafeDto.getLanguageNames().contains(language.getName())) {
                    cafesLanguages.add(language);
                }
            }
            cafe.setLanguages(cafesLanguages);
        }
        if (createCafeDto.isBirthday() != cafe.isBirthday()) {
            cafe.setBirthday(createCafeDto.isBirthday());
        }
        if (createCafeDto.isCoworking() != cafe.isCoworking()) {
            cafe.setCoworking(createCafeDto.isCoworking());
        }
        if (createCafeDto.isFastService() != cafe.isFastService()) {
            cafe.setFastService(createCafeDto.isFastService());
        }
        if (createCafeDto.isParking() != cafe.isParking()) {
            cafe.setParking(createCafeDto.isParking());
        }
        if (createCafeDto.isBoardGames() != cafe.isBoardGames()) {
            cafe.setBoardGames(createCafeDto.isBoardGames());
        }
        if (createCafeDto.isBusinessLunch() != cafe.isBusinessLunch()) {
            cafe.setBusinessLunch(createCafeDto.isBusinessLunch());
        }
        if (createCafeDto.isTerrace() != cafe.isTerrace()) {
            cafe.setTerrace(createCafeDto.isTerrace());
        }
        if (createCafeDto.isRomantic() != cafe.isRomantic()) {
            cafe.setRomantic(createCafeDto.isRomantic());
        }
        if (createCafeDto.isVegan() != cafe.isVegan()) {
            cafe.setVegan(createCafeDto.isVegan());
        }
        if (createCafeDto.isFamilyHoliday() != cafe.isFamilyHoliday()) {
            cafe.setFamilyHoliday(createCafeDto.isFamilyHoliday());
        }
        if (createCafeDto.isWifi() != cafe.isWifi()) {
            cafe.setWifi(createCafeDto.isWifi());
        }
        if (createCafeDto.isFreeWater() != cafe.isFreeWater()) {
            cafe.setFreeWater(createCafeDto.isFreeWater());
        }
        if (createCafeDto.isThematicEvent() != cafe.isThematicEvent()) {
            cafe.setThematicEvent(createCafeDto.isThematicEvent());
        }
        if (createCafeDto.isPetFriendly() != cafe.isPetFriendly()) {
            cafe.setPetFriendly(createCafeDto.isPetFriendly());
        }
        CafeResponseDto cafeDto = cafeMapper.toDto(cafe);
        orderComments(List.of(cafeDto));
        return cafeDto;
    }

    private Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no cafe with id " + cafeId));
    }

    private void orderComments(List<CafeResponseDto> cafes) {
        cafes.forEach(e ->
                e.getComments().sort(Comparator.comparing(CommentResponseDto::getId).reversed()));
    }
}
