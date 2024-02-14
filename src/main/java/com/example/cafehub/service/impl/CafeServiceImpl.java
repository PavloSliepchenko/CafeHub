package com.example.cafehub.service.impl;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.specification.CafeSpecificationBuilder;
import com.example.cafehub.service.CafeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CafeServiceImpl implements CafeService {
    private final CafeSpecificationBuilder specificationBuilder;
    private final CafeRepository cafeRepository;
    private final CafeMapper cafeMapper;

    @Override
    public List<CafeResponseDto> getAllCafes(Pageable pageable) {
        return cafeRepository.findAll(pageable).stream()
                .map(cafeMapper::toDto)
                .toList();
    }

    @Override
    public CafeResponseDto getCafeById(Long cafeId) {
        return cafeMapper.toDto(findCafeById(cafeId));
    }

    @Override
    public CafeResponseDto addCafe(CreateCafeDto createCafeDto) {
        Cafe cafe = cafeMapper.toModel(createCafeDto);
        cafe.setScore(0);
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
    public void deleteCafe(Long cafeId) {
        cafeRepository.delete(findCafeById(cafeId));
    }

    private Cafe findCafeById(Long cafeId) {
        return cafeRepository.findById(cafeId)
                .orElseThrow(() ->
                        new EntityNotFoundException("There is no cafe with id " + cafeId));
    }
}
