package com.example.cafehub.service.impl;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.exception.EntityAlreadyExistsException;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.model.Score;
import com.example.cafehub.model.User;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.ScoreRepository;
import com.example.cafehub.repository.specification.CafeSpecificationBuilder;
import com.example.cafehub.service.CafeService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeSpecificationBuilder specificationBuilder;
    private final ScoreRepository scoreRepository;
    private final CafeRepository cafeRepository;
    private final CafeMapper cafeMapper;

    @Override
    public List<CafeResponseDto> getAllCafes(Pageable pageable) {
        return cafeRepository.findAll(pageable).stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public List<CafeResponseDto> getAllCafesInCity(String cityName, Pageable pageable) {
        return cafeRepository.findByCity(cityName, pageable).stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public CafeResponseDto getCafeById(Long cafeId) {
        return cafeMapper.toDto(findCafeById(cafeId));
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
    public CafeResponseDto updateCafeInfo(Long cafeId, CreateCafeDto createCafeDto) {
        if (!cafeRepository.existsById(cafeId)) {
            throw new EntityNotFoundException("There is no cafe with id " + cafeId);
        }
        Cafe cafe = cafeMapper.toModel(createCafeDto);
        cafe.setId(cafeId);
        return cafeMapper.toDto(cafeRepository.save(cafe));
    }

    @Override
    public List<CafeResponseDto> cafeSearch(CafeSearchParametersDto searchParametersDto) {
        Specification<Cafe> cafeSpecification = specificationBuilder.build(searchParametersDto);
        return cafeRepository.findAll(cafeSpecification).stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public CafeResponseDto setScore(Long userId, Long cafeId, BigDecimal score) {
        Optional<Score> scoreOptional = scoreRepository.findByUserIdAndCafeId(userId, cafeId);
        Cafe cafe = findCafeById(cafeId);
        if (scoreOptional.isEmpty()) {
            User user = new User();
            user.setId(userId);
            Score scoreEntity = new Score();
            scoreEntity.setCafe(cafe);
            scoreEntity.setUser(user);
            scoreEntity.setScore(score);
            scoreRepository.save(scoreEntity);
            cafe.setNumberOfUsersVoted(cafe.getNumberOfUsersVoted().add(BigDecimal.ONE));
            cafe.setTotalScore(cafe.getTotalScore().add(score));
            cafe.setScore(cafe.getTotalScore().divide(cafe.getNumberOfUsersVoted()));
        } else {
            Score scoreEntity = scoreOptional.get();
            BigDecimal newTotalScore = cafe.getTotalScore()
                    .subtract(scoreEntity.getScore())
                    .add(score);
            cafe.setTotalScore(newTotalScore);
            cafe.setScore(newTotalScore.divide(cafe.getNumberOfUsersVoted()));
            scoreEntity.setScore(score);
            scoreRepository.save(scoreEntity);
        }
        return cafeMapper.toDto(cafeRepository.save(cafe));
    }

    @Override
    public void deleteCafe(Long cafeId) {
        cafeRepository.delete(findCafeById(cafeId));
    }

    private Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no cafe with id " + cafeId));
    }
}
