package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Phrase;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PhraseRepositoryWithBagRelationshipsImpl implements PhraseRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Phrase> fetchBagRelationships(Optional<Phrase> phrase) {
        return phrase.map(this::fetchCategories).map(this::fetchTags);
    }

    @Override
    public Page<Phrase> fetchBagRelationships(Page<Phrase> phrases) {
        return new PageImpl<>(fetchBagRelationships(phrases.getContent()), phrases.getPageable(), phrases.getTotalElements());
    }

    @Override
    public List<Phrase> fetchBagRelationships(List<Phrase> phrases) {
        return Optional.of(phrases).map(this::fetchCategories).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Phrase fetchCategories(Phrase result) {
        return entityManager
            .createQuery("select phrase from Phrase phrase left join fetch phrase.categories where phrase.id = :id", Phrase.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Phrase> fetchCategories(List<Phrase> phrases) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, phrases.size()).forEach(index -> order.put(phrases.get(index).getId(), index));
        List<Phrase> result = entityManager
            .createQuery("select phrase from Phrase phrase left join fetch phrase.categories where phrase in :phrases", Phrase.class)
            .setParameter("phrases", phrases)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Phrase fetchTags(Phrase result) {
        return entityManager
            .createQuery("select phrase from Phrase phrase left join fetch phrase.tags where phrase.id = :id", Phrase.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Phrase> fetchTags(List<Phrase> phrases) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, phrases.size()).forEach(index -> order.put(phrases.get(index).getId(), index));
        List<Phrase> result = entityManager
            .createQuery("select phrase from Phrase phrase left join fetch phrase.tags where phrase in :phrases", Phrase.class)
            .setParameter("phrases", phrases)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
