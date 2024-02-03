package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Tag;
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
public class TagRepositoryWithBagRelationshipsImpl implements TagRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> fetchBagRelationships(Optional<Tag> tag) {
        return tag.map(this::fetchCategories);
    }

    @Override
    public Page<Tag> fetchBagRelationships(Page<Tag> tags) {
        return new PageImpl<>(fetchBagRelationships(tags.getContent()), tags.getPageable(), tags.getTotalElements());
    }

    @Override
    public List<Tag> fetchBagRelationships(List<Tag> tags) {
        return Optional.of(tags).map(this::fetchCategories).orElse(Collections.emptyList());
    }

    Tag fetchCategories(Tag result) {
        return entityManager
            .createQuery("select tag from Tag tag left join fetch tag.categories where tag.id = :id", Tag.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Tag> fetchCategories(List<Tag> tags) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, tags.size()).forEach(index -> order.put(tags.get(index).getId(), index));
        List<Tag> result = entityManager
            .createQuery("select tag from Tag tag left join fetch tag.categories where tag in :tags", Tag.class)
            .setParameter("tags", tags)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
