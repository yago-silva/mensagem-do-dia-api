package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.Tag;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.dto.TagDTO;
import com.mensagemdodia.service.dto.UserDTO;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    @Mapping(source = "phrases", target = "lastPhraseUpdate", qualifiedByName = "phrasesToLastPhraseUpdate")
    TagDTO toDto(Tag s);

    @Named("phrasesToLastPhraseUpdate")
    public static Instant phrasesToLastPhraseUpdate(Set<Phrase> phrases) {
        List<Instant> sortedPhrases = phrases.stream().map(Phrase::getUpdatedAt).sorted().toList();

        if (sortedPhrases.isEmpty()) {
            return null;
        }

        return sortedPhrases.get(sortedPhrases.size() - 1);
    }

    @Mapping(target = "removeCategory", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }
}
