package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Phrase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Phrase entity.
 *
 * When extending this class, extend PhraseRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface PhraseRepository extends PhraseRepositoryWithBagRelationships, JpaRepository<Phrase, Long> {
    default Optional<Phrase> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Phrase> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Phrase> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(
        "SELECT " +
        "p FROM Phrase p " +
        "LEFT JOIN p.categories c " +
        "LEFT JOIN p.tags t " +
        "WHERE " +
        "( " +
        "(c.slug = :slug AND c.active = true) " +
        "OR " +
        "(t.slug = :slug AND t.active = true) " +
        ") " +
        "AND " +
        "p.active = true"
    )
    public List<Phrase> getAllByGroupSlug(@Param("slug") String slug);
}
