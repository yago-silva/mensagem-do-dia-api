package com.mensagemdodia.repository;

import com.mensagemdodia.domain.Category;
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
    @Query("SELECT c FROM Category c WHERE c.featured = true AND c.active = true ORDER BY c.updatedAt DESC")
    public List<Category> getAllFeatured();

    @Query("SELECT c FROM Category c WHERE (c.active = true OR :includeInactives = true) AND c.slug = :slug ORDER BY c.updatedAt DESC")
    Optional<Category> findBySlug(String slug, @Param("includeInactives") boolean includeInactives);

    @Query("SELECT c FROM Category c WHERE (c.active = true OR :includeInactives = true) AND c.category.id = :id ORDER BY c.createdAt DESC")
    List<Category> findChildCategoryByParentCategoryId(Long id, @Param("includeInactives") boolean includeInactives);

    @Query("SELECT c FROM Category c WHERE c.id in (:ids) AND c.active = true ORDER BY c.updatedAt DESC")
    public List<Category> getAllByIds(List<Long> ids);
}
