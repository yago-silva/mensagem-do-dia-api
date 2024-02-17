package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Author entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.active = true AND slug = :slug")
    Optional<Author> findBySlug(String slug);
}
