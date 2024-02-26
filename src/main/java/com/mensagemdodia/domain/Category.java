package com.mensagemdodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "featured", nullable = false)
    private Boolean featured;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "slug", nullable = false)
    private String slug;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnoreProperties(value = { "owner", "parents", "media", "phrases", "ads", "tags", "category" }, allowSetters = true)
    private Set<Category> parents = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnoreProperties(value = { "owner", "phrase", "ad", "category", "tag" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(value = { "owner", "author", "media", "categories", "tags" }, allowSetters = true)
    private Set<Phrase> phrases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(value = { "owner", "media", "categories", "tags", "authors" }, allowSetters = true)
    private Set<Ad> ads = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    @JsonIgnoreProperties(value = { "owner", "media", "categories", "phrases", "ads" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "owner", "parents", "media", "phrases", "ads", "tags", "category" }, allowSetters = true)
    private Category category;

    private String description;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Category id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Category name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Category createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Category updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public Category featured(Boolean featured) {
        this.setFeatured(featured);
        return this;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Category active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSlug() {
        return this.slug;
    }

    public Category slug(String slug) {
        this.setSlug(slug);
        return this;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Category owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<Category> getParents() {
        return this.parents;
    }

    public void setParents(Set<Category> categories) {
        if (this.parents != null) {
            this.parents.forEach(i -> i.setCategory(null));
        }
        if (categories != null) {
            categories.forEach(i -> i.setCategory(this));
        }
        this.parents = categories;
    }

    public Category parents(Set<Category> categories) {
        this.setParents(categories);
        return this;
    }

    public Category addParent(Category category) {
        this.parents.add(category);
        category.setCategory(this);
        return this;
    }

    public Category removeParent(Category category) {
        this.parents.remove(category);
        category.setCategory(null);
        return this;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setCategory(null));
        }
        if (media != null) {
            media.forEach(i -> i.setCategory(this));
        }
        this.media = media;
    }

    public Category media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Category addMedia(Media media) {
        this.media.add(media);
        media.setCategory(this);
        return this;
    }

    public Category removeMedia(Media media) {
        this.media.remove(media);
        media.setCategory(null);
        return this;
    }

    public Set<Phrase> getPhrases() {
        return this.phrases;
    }

    public void setPhrases(Set<Phrase> phrases) {
        if (this.phrases != null) {
            this.phrases.forEach(i -> i.removeCategory(this));
        }
        if (phrases != null) {
            phrases.forEach(i -> i.addCategory(this));
        }
        this.phrases = phrases;
    }

    public Category phrases(Set<Phrase> phrases) {
        this.setPhrases(phrases);
        return this;
    }

    public Category addPhrase(Phrase phrase) {
        this.phrases.add(phrase);
        phrase.getCategories().add(this);
        return this;
    }

    public Category removePhrase(Phrase phrase) {
        this.phrases.remove(phrase);
        phrase.getCategories().remove(this);
        return this;
    }

    public Set<Ad> getAds() {
        return this.ads;
    }

    public void setAds(Set<Ad> ads) {
        if (this.ads != null) {
            this.ads.forEach(i -> i.removeCategory(this));
        }
        if (ads != null) {
            ads.forEach(i -> i.addCategory(this));
        }
        this.ads = ads;
    }

    public Category ads(Set<Ad> ads) {
        this.setAds(ads);
        return this;
    }

    public Category addAd(Ad ad) {
        this.ads.add(ad);
        ad.getCategories().add(this);
        return this;
    }

    public Category removeAd(Ad ad) {
        this.ads.remove(ad);
        ad.getCategories().remove(this);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        if (this.tags != null) {
            this.tags.forEach(i -> i.removeCategory(this));
        }
        if (tags != null) {
            tags.forEach(i -> i.addCategory(this));
        }
        this.tags = tags;
    }

    public Category tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Category addTag(Tag tag) {
        this.tags.add(tag);
        tag.getCategories().add(this);
        return this;
    }

    public Category removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getCategories().remove(this);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return getId() != null && getId().equals(((Category) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", featured='" + getFeatured() + "'" +
            ", active='" + getActive() + "'" +
            ", slug='" + getSlug() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
