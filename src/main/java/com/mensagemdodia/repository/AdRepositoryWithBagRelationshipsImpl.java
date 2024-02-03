package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Ad;
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
public class AdRepositoryWithBagRelationshipsImpl implements AdRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ad> fetchBagRelationships(Optional<Ad> ad) {
        return ad.map(this::fetchCategories).map(this::fetchTags).map(this::fetchAuthors);
    }

    @Override
    public Page<Ad> fetchBagRelationships(Page<Ad> ads) {
        return new PageImpl<>(fetchBagRelationships(ads.getContent()), ads.getPageable(), ads.getTotalElements());
    }

    @Override
    public List<Ad> fetchBagRelationships(List<Ad> ads) {
        return Optional.of(ads).map(this::fetchCategories).map(this::fetchTags).map(this::fetchAuthors).orElse(Collections.emptyList());
    }

    Ad fetchCategories(Ad result) {
        return entityManager
            .createQuery("select ad from Ad ad left join fetch ad.categories where ad.id = :id", Ad.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ad> fetchCategories(List<Ad> ads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ads.size()).forEach(index -> order.put(ads.get(index).getId(), index));
        List<Ad> result = entityManager
            .createQuery("select ad from Ad ad left join fetch ad.categories where ad in :ads", Ad.class)
            .setParameter("ads", ads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Ad fetchTags(Ad result) {
        return entityManager
            .createQuery("select ad from Ad ad left join fetch ad.tags where ad.id = :id", Ad.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ad> fetchTags(List<Ad> ads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ads.size()).forEach(index -> order.put(ads.get(index).getId(), index));
        List<Ad> result = entityManager
            .createQuery("select ad from Ad ad left join fetch ad.tags where ad in :ads", Ad.class)
            .setParameter("ads", ads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }

    Ad fetchAuthors(Ad result) {
        return entityManager
            .createQuery("select ad from Ad ad left join fetch ad.authors where ad.id = :id", Ad.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ad> fetchAuthors(List<Ad> ads) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ads.size()).forEach(index -> order.put(ads.get(index).getId(), index));
        List<Ad> result = entityManager
            .createQuery("select ad from Ad ad left join fetch ad.authors where ad in :ads", Ad.class)
            .setParameter("ads", ads)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
