package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Category} and its DTO {@link CategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface CategoryMapper extends EntityMapper<CategoryDTO, Category> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    CategoryDTO toDto(Category s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
