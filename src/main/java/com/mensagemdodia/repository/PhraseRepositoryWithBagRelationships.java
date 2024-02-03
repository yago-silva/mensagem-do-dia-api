package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Phrase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PhraseRepositoryWithBagRelationships {
    Optional<Phrase> fetchBagRelationships(Optional<Phrase> phrase);

    List<Phrase> fetchBagRelationships(List<Phrase> phrases);

    Page<Phrase> fetchBagRelationships(Page<Phrase> phrases);
}
