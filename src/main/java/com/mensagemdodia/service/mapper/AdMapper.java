package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Ad;
import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Tag;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.AdDTO;
import com.mensagemdodia.service.dto.AuthorDTO;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.dto.TagDTO;
import com.mensagemdodia.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ad} and its DTO {@link AdDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdMapper extends EntityMapper<AdDTO, Ad> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoryIdSet")
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdSet")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "authorIdSet")
    AdDTO toDto(Ad s);

    @Mapping(target = "removeCategory", ignore = true)
    @Mapping(target = "removeTag", ignore = true)
    @Mapping(target = "removeAuthor", ignore = true)
    Ad toEntity(AdDTO adDTO);

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

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);

    @Named("tagIdSet")
    default Set<TagDTO> toDtoTagIdSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagId).collect(Collectors.toSet());
    }

    @Named("authorId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AuthorDTO toDtoAuthorId(Author author);

    @Named("authorIdSet")
    default Set<AuthorDTO> toDtoAuthorIdSet(Set<Author> author) {
        return author.stream().map(this::toDtoAuthorId).collect(Collectors.toSet());
    }
}
