package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Ad;
import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Media;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.Tag;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.AdDTO;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.dto.MediaDTO;
import com.mensagemdodia.service.dto.PhraseDTO;
import com.mensagemdodia.service.dto.TagDTO;
import com.mensagemdodia.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Media} and its DTO {@link MediaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MediaMapper extends EntityMapper<MediaDTO, Media> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "phrase", source = "phrase", qualifiedByName = "phraseId")
    @Mapping(target = "ad", source = "ad", qualifiedByName = "adId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    @Mapping(target = "tag", source = "tag", qualifiedByName = "tagId")
    MediaDTO toDto(Media s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("phraseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PhraseDTO toDtoPhraseId(Phrase phrase);

    @Named("adId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AdDTO toDtoAdId(Ad ad);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);
}
