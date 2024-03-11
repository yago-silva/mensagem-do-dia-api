package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.*;
import com.mensagemdodia.service.dto.*;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Phrase} and its DTO {@link PhraseDTO}.
 */
@Mapper(componentModel = "spring")
public interface PhraseMapper extends EntityMapper<PhraseDTO, Phrase> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "author", source = "author", qualifiedByName = "authorId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdSet")
    @Mapping(target = "media", source = "media", qualifiedByName = "mediaIdSet")
    PhraseDTO toDto(Phrase s);

    @Mapping(target = "removeCategory", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    Phrase toEntity(PhraseDTO phraseDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("authorId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    AuthorDTO toDtoAuthorId(Author author);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("categoryIdSet")
    default Set<CategoryDTO> toDtoCategoryIdSet(Set<Category> category) {
        return category.stream().map(this::toDtoCategoryId).collect(Collectors.toSet());
    }

    @Named("tagId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);

    @Named("tagIdSet")
    default Set<TagDTO> toDtoTagIdSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagId).collect(Collectors.toSet());
    }

    @Named("mediaId")
    @BeanMapping(ignoreByDefault = false)
    @Mapping(target = "id", source = "id")
    PhraseMediaDTO toDtoMediaId(Media media);

    @Named("mediaIdSet")
    default Set<PhraseMediaDTO> toDtoMediaIdSet(Set<Media> media) {
        return media.stream().map(this::toDtoMediaId).collect(Collectors.toSet());
    }
}
