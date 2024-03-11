package com.mensagemdodia.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Base64;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mensagemdodia.domain.Phrase} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PhraseDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    @NotNull
    private Instant createdAt = Instant.now();

    @NotNull
    private Instant updatedAt = Instant.now();

    @NotNull
    private Boolean featured;

    @NotNull
    private Boolean active;

    @NotNull
    private String slug;

    private UserDTO owner;

    private AuthorDTO author;

    private Set<CategoryDTO> categories = new HashSet<>();

    private Set<TagDTO> tags = new HashSet<>();

    private Set<PhraseMediaDTO> media = new HashSet<>();

    private String mainMediaBase64;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFeatured() {
        return featured;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PhraseDTO)) {
            return false;
        }

        PhraseDTO phraseDTO = (PhraseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, phraseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PhraseDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", featured='" + getFeatured() + "'" +
            ", active='" + getActive() + "'" +
            ", slug='" + getSlug() + "'" +
            ", owner=" + getOwner() +
            ", author=" + getAuthor() +
            ", categories=" + getCategories() +
            ", tags=" + getTags() +
            ", media=" + getMedia() +
            "}";
    }

    public Set<PhraseMediaDTO> getMedia() {
        return media;
    }

    public void setMedia(Set<PhraseMediaDTO> media) {
        this.media = media;
    }

    public String getMainMediaBase64() {
        return mainMediaBase64;
    }

    public void setMainMediaBase64(String mainMediaBase64) {
        this.mainMediaBase64 = mainMediaBase64;
    }
}
