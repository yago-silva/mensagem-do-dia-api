package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Phrase;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Category entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.featured = true AND c.active = true")
    public List<Category> getAllFeatured();

    @Query("SELECT c FROM Category c WHERE c.active = true AND c.slug = :slug")
    Optional<Category> findBySlug(String slug);
}
