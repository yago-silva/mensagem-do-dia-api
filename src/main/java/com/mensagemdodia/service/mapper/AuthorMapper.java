package com.mensagemdodia.service.mapper;

import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.User;
import com.mensagemdodia.service.dto.AuthorDTO;
import com.mensagemdodia.service.dto.UserDTO;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Author} and its DTO {@link AuthorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuthorMapper extends EntityMapper<AuthorDTO, Author> {
    @Mapping(target = "owner", source = "owner", qualifiedByName = "userId")
    @Mapping(source = "phrases", target = "lastPhraseUpdate", qualifiedByName = "phrasesToLastPhraseUpdate")
    AuthorDTO toDto(Author s);

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
}
