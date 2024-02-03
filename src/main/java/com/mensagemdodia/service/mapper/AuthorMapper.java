package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.AuthorDTO;
import com.mensagemdodia.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    AuthorDTO toDto(Author s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
