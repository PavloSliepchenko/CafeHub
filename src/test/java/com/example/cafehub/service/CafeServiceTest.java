package com.example.cafehub.service;

import com.example.cafehub.dto.cafe.CafeResponseDto;
import com.example.cafehub.dto.cafe.CafeSearchParametersDto;
import com.example.cafehub.dto.cafe.CreateCafeDto;
import com.example.cafehub.exception.EntityAlreadyExistsException;
import com.example.cafehub.exception.EntityNotFoundException;
import com.example.cafehub.mapper.CafeMapper;
import com.example.cafehub.model.Cafe;
import com.example.cafehub.repository.CafeRepository;
import com.example.cafehub.repository.specification.CafeSpecificationBuilder;
import com.example.cafehub.service.impl.CafeServiceImpl;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class CafeServiceTest {
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;
    private CafeResponseDto responseDto1;
    private CafeResponseDto responseDto2;
    private CafeResponseDto responseDto3;
    private CreateCafeDto createCafeDto;
    @Mock
    private CafeSpecificationBuilder specificationBuilder;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CafeMapper cafeMapper;
    @InjectMocks
    private CafeServiceImpl cafeService;

    @BeforeEach
    private void init() {
        cafe1 = new Cafe();
        cafe1.setId(1L);
        cafe1.setName("Cafe 1");
        cafe1.setAddress("233rd st");
        cafe1.setCity("Kyiv");
        cafe1.setScore(BigDecimal.valueOf(4));
        cafe1.setComments(new ArrayList<>());

        cafe2 = new Cafe();
        cafe2.setId(2L);
        cafe2.setName("Cafe 2");
        cafe2.setAddress("134th st");
        cafe2.setCity("Lviv");
        cafe2.setScore(BigDecimal.valueOf(3));
        cafe2.setComments(new ArrayList<>());

        cafe3 = new Cafe();
        cafe3.setId(3L);
        cafe3.setName("Cafe 3");
        cafe3.setAddress("122nd st");
        cafe3.setCity("Kyiv");
        cafe3.setScore(BigDecimal.valueOf(5));
        cafe3.setComments(new ArrayList<>());

        responseDto1 = new CafeResponseDto();
        responseDto1.setId(cafe1.getId());
        responseDto1.setName(cafe1.getName());
        responseDto1.setAddress(cafe1.getAddress());
        responseDto1.setScore(cafe1.getScore());
        responseDto1.setComments(new ArrayList<>());

        responseDto2 = new CafeResponseDto();
        responseDto2.setId(cafe2.getId());
        responseDto2.setName(cafe2.getName());
        responseDto2.setAddress(cafe2.getAddress());
        responseDto2.setScore(cafe2.getScore());
        responseDto2.setComments(new ArrayList<>());

        responseDto3 = new CafeResponseDto();
        responseDto3.setId(cafe3.getId());
        responseDto3.setName(cafe3.getName());
        responseDto3.setAddress(cafe3.getAddress());
        responseDto3.setScore(cafe3.getScore());
        responseDto3.setComments(new ArrayList<>());

        createCafeDto = new CreateCafeDto();
        createCafeDto.setName("New cafe");
        createCafeDto.setCity("Poltava");
        createCafeDto.setAddress("23rd st, building 3");
    }

    @Test
    @DisplayName("Get all cafes")
    void getAllCafes_ValidRequest_ShouldReturnListOfDtos() {
        List<Cafe> cafeList = List.of(cafe1, cafe2, cafe3);
        Pageable pageable = PageRequest.of(0, 5);
        Page<Cafe> cafesPage = new PageImpl<>(cafeList, pageable, cafeList.size());
        Mockito.when(cafeRepository.findAll(pageable)).thenReturn(cafesPage);
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(responseDto1);
        Mockito.when(cafeMapper.toDto(cafe2)).thenReturn(responseDto2);
        Mockito.when(cafeMapper.toDto(cafe3)).thenReturn(responseDto3);

        List<CafeResponseDto> expected = List.of(responseDto1, responseDto2, responseDto3);
        List<CafeResponseDto> actual = cafeService.getAllCafes(pageable);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Get all cafes in the city")
    void getAllCafesInCity_ValidRequest_ShouldReturnListOfDtos() {
        List<Cafe> cafeList = List.of(cafe1, cafe3);
        Pageable pageable = PageRequest.of(0, 5);
        Mockito.when(cafeRepository.findByCity("Kyiv", pageable)).thenReturn(cafeList);
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(responseDto1);
        Mockito.when(cafeMapper.toDto(cafe3)).thenReturn(responseDto3);

        List<CafeResponseDto> expected = List.of(responseDto1, responseDto3);
        List<CafeResponseDto> actual = cafeService.getAllCafesInCity("Kyiv", pageable);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Get cafe by id")
    void getCafeById_ValidRequest_ShouldReturnCafeDto() {
        Mockito.when(cafeRepository.findById(3L)).thenReturn(Optional.of(cafe3));
        Mockito.when(cafeMapper.toDto(cafe3)).thenReturn(responseDto3);

        CafeResponseDto actual = cafeService.getCafeById(3L);

        Assertions.assertEquals(responseDto3.getId(), actual.getId());
        Assertions.assertEquals(responseDto3.getName(), actual.getName());
        Assertions.assertEquals(responseDto3.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Get cafe by id. Wrong id. Throws exception")
    void getCafeById_WrongCafeId_ShouldThrowException() {
        Mockito.when(cafeRepository.findById(4L)).thenReturn(Optional.empty());
        Mockito.when(cafeRepository.findById(15L)).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class, () -> cafeService.getCafeById(4L));
        Assertions.assertThrows(EntityNotFoundException.class, () -> cafeService.getCafeById(15L));
    }

    @Test
    @DisplayName("Add a new cafe")
    void addCafe_ValidRequest_ShouldAddCafe() {
        Cafe cafe = new Cafe();
        cafe.setId(5L);
        cafe.setName(createCafeDto.getName());
        cafe.setCity(createCafeDto.getCity());
        cafe.setAddress(createCafeDto.getAddress());

        CafeResponseDto responseDto = new CafeResponseDto();
        responseDto.setId(cafe.getId());
        responseDto.setName(cafe.getName());
        responseDto.setAddress(cafe.getAddress());

        Mockito.when(cafeRepository.save(cafe)).thenReturn(cafe);
        Mockito.when(cafeMapper.toDto(cafe)).thenReturn(responseDto);
        Mockito.when(cafeMapper.toModel(createCafeDto)).thenReturn(cafe);

        CafeResponseDto actual = cafeService.addCafe(createCafeDto);

        Assertions.assertEquals(responseDto.getId(), actual.getId());
        Assertions.assertEquals(responseDto.getName(), actual.getName());
        Assertions.assertEquals(responseDto.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Add a new cafe. Cafe was added before. Throws exception")
    void addCafe_CafeAlreadyAdded_ShouldThrowException() {
        Mockito.when(cafeRepository.findByNameAndAddressAndCity(Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Optional.of(Mockito.mock(Cafe.class)));

        Assertions.assertThrows(EntityAlreadyExistsException.class,
                () -> cafeService.addCafe(createCafeDto));
    }

    @Test
    @DisplayName("Update cafe info")
    void updateCafeInfo_ValidRequest_ShouldReturnUpdatedResponseDto() {
        cafe2.setName(createCafeDto.getName());
        cafe2.setCity(createCafeDto.getCity());
        cafe2.setAddress(createCafeDto.getAddress());

        responseDto2.setName(cafe2.getName());
        responseDto2.setAddress(cafe2.getName());

        Long cafeId = 2L;
        Mockito.when(cafeRepository.findById(cafeId)).thenReturn(Optional.of(cafe2));
        Mockito.when(cafeMapper.toDto(cafe2)).thenReturn(responseDto2);

        CafeResponseDto actual = cafeService.updateCafeInfo(cafeId, createCafeDto);

        Assertions.assertEquals(responseDto2.getId(), actual.getId());
        Assertions.assertEquals(responseDto2.getName(), actual.getName());
        Assertions.assertEquals(responseDto2.getAddress(), actual.getAddress());
    }

    @Test
    @DisplayName("Update cafe info. Wrong id. Throws exception")
    void updateCafeInfo_WrongCafeId_ShouldThrowException() {
        Mockito.when(cafeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> cafeService.updateCafeInfo(45L, createCafeDto));
    }

    @Test
    @DisplayName("Cafe search with parameters")
    void cafeSearch_ValidRequest_ShouldReturnListOfDtos() {
        List<Cafe> cafes = List.of(cafe1, cafe3);
        CafeSearchParametersDto searchParameters = Mockito.mock(CafeSearchParametersDto.class);
        Specification<Cafe> cafeSpecification = Mockito.mock(Specification.class);
        Mockito.when(specificationBuilder.build(searchParameters)).thenReturn(cafeSpecification);
        Mockito.when(cafeRepository.findAll(cafeSpecification)).thenReturn(cafes);
        Mockito.when(cafeMapper.toDto(cafe1)).thenReturn(responseDto1);
        Mockito.when(cafeMapper.toDto(cafe3)).thenReturn(responseDto3);

        List<CafeResponseDto> expected = List.of(responseDto1, responseDto3);
        List<CafeResponseDto> actual = cafeService.cafeSearch(searchParameters);

        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertTrue(actual.containsAll(expected));
    }

    @Test
    @DisplayName("Delete cafe. Wrong id. Throws exception")
    void deleteCafe_WrongId_ShouldThrowException() {
        Mockito.when(cafeRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> cafeService.deleteCafe(Mockito.anyLong()));
    }
}
