package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.active = true AND a.slug = :slug ORDER BY a.updatedAt DESC")
    Optional<Author> findBySlug(String slug);

    @Query("SELECT a FROM Author a WHERE a.featured = true AND a.active = true ORDER BY a.updatedAt DESC")
    public List<Author> getAllFeatured();

    @Query("SELECT a FROM Author a WHERE a.active = true ORDER BY a.updatedAt DESC")
    public List<Author> getAllActive();
}
