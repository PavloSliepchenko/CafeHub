package com.example.cafehub.mapper;

import com.example.cafehub.config.MapperConfig;
import com.example.cafehub.dto.language.LanguageCreateRequestDto;
import com.example.cafehub.dto.language.LanguageResponseDto;
import com.example.cafehub.model.Language;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface LanguageMapper {
    LanguageResponseDto toDto(Language language);

    Language toModel(LanguageCreateRequestDto createRequestDto);

    @Named("getByLanguageName")
    default List<Language> getByLanguageName(List<String> languageNames) {
        if (languageNames == null) {
            return null;
        }

        List<Language> languages = new ArrayList<>();
        for (String name: languageNames) {
            if (name.equalsIgnoreCase("Ukrainian")) {
                Language language = new Language();
                language.setId(1L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("English")) {
                Language language = new Language();
                language.setId(2L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Polish")) {
                Language language = new Language();
                language.setId(3L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Russian")) {
                Language language = new Language();
                language.setId(4L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("French")) {
                Language language = new Language();
                language.setId(5L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("German")) {
                Language language = new Language();
                language.setId(6L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Turkish")) {
                Language language = new Language();
                language.setId(7L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Chinese")) {
                Language language = new Language();
                language.setId(8L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Italian")) {
                Language language = new Language();
                language.setId(9L);
                languages.add(language);
            } else if (name.equalsIgnoreCase("Portuguese")) {
                Language language = new Language();
                language.setId(10L);
                languages.add(language);
            }
        }

        return languages;
    }
}
