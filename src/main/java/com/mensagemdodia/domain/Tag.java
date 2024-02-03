package com.mensagemdodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Tag.
 */
@Entity
@Table(name = "tag")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tag implements Serializable {

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tag")
    @JsonIgnoreProperties(value = { "owner", "phrase", "ad", "category", "tag" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_tag__category",
        joinColumns = @JoinColumn(name = "tag_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnoreProperties(value = { "owner", "parents", "media", "phrases", "ads", "tags", "category" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @JsonIgnoreProperties(value = { "owner", "author", "media", "categories", "tags" }, allowSetters = true)
    private Set<Phrase> phrases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    @JsonIgnoreProperties(value = { "owner", "media", "categories", "tags", "authors" }, allowSetters = true)
    private Set<Ad> ads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Tag name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Tag createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Tag updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public Tag featured(Boolean featured) {
        this.setFeatured(featured);
        return this;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Tag active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSlug() {
        return this.slug;
    }

    public Tag slug(String slug) {
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

    public Tag owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setTag(null));
        }
        if (media != null) {
            media.forEach(i -> i.setTag(this));
        }
        this.media = media;
    }

    public Tag media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Tag addMedia(Media media) {
        this.media.add(media);
        media.setTag(this);
        return this;
    }

    public Tag removeMedia(Media media) {
        this.media.remove(media);
        media.setTag(null);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Tag categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Tag addCategory(Category category) {
        this.categories.add(category);
        return this;
    }

    public Tag removeCategory(Category category) {
        this.categories.remove(category);
        return this;
    }

    public Set<Phrase> getPhrases() {
        return this.phrases;
    }

    public void setPhrases(Set<Phrase> phrases) {
        if (this.phrases != null) {
            this.phrases.forEach(i -> i.removeTag(this));
        }
        if (phrases != null) {
            phrases.forEach(i -> i.addTag(this));
        }
        this.phrases = phrases;
    }

    public Tag phrases(Set<Phrase> phrases) {
        this.setPhrases(phrases);
        return this;
    }

    public Tag addPhrase(Phrase phrase) {
        this.phrases.add(phrase);
        phrase.getTags().add(this);
        return this;
    }

    public Tag removePhrase(Phrase phrase) {
        this.phrases.remove(phrase);
        phrase.getTags().remove(this);
        return this;
    }

    public Set<Ad> getAds() {
        return this.ads;
    }

    public void setAds(Set<Ad> ads) {
        if (this.ads != null) {
            this.ads.forEach(i -> i.removeTag(this));
        }
        if (ads != null) {
            ads.forEach(i -> i.addTag(this));
        }
        this.ads = ads;
    }

    public Tag ads(Set<Ad> ads) {
        this.setAds(ads);
        return this;
    }

    public Tag addAd(Ad ad) {
        this.ads.add(ad);
        ad.getTags().add(this);
        return this;
    }

    public Tag removeAd(Ad ad) {
        this.ads.remove(ad);
        ad.getTags().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tag)) {
            return false;
        }
        return getId() != null && getId().equals(((Tag) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tag{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", featured='" + getFeatured() + "'" +
            ", active='" + getActive() + "'" +
            ", slug='" + getSlug() + "'" +
            "}";
    }
}
