package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.dto.UserDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(source = "phrases", target = "lastPhraseUpdate", qualifiedByName = "phrasesToLastPhraseUpdate")
    CategoryDTO toDto(Category s);

    @Named("phrasesToLastPhraseUpdate")
    public static Instant phrasesToLastPhraseUpdate(Set<Phrase> phrases) {
        List<Instant> sortedPhrases = phrases.stream().map(Phrase::getUpdatedAt).sorted().toList();

        if (sortedPhrases.isEmpty()) {
            return null;
        }

        return sortedPhrases.get(sortedPhrases.size() - 1);
    }

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
