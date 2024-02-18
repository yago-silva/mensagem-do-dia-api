package com.mensagemdodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Author.
 */
@Entity
@Table(name = "author")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Author implements Serializable {

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

    @JsonIgnoreProperties(value = { "owner", "author", "media", "categories", "tags" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "author")
    private Phrase phrase;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "author")
    @JsonIgnoreProperties(value = { "owner", "author", "media", "categories", "tags", "author" }, allowSetters = true)
    private Set<Phrase> phrases = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "authors")
    @JsonIgnoreProperties(value = { "owner", "media", "categories", "tags", "authors" }, allowSetters = true)
    private Set<Ad> ads = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Author id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Author name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Author createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Author updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public Author featured(Boolean featured) {
        this.setFeatured(featured);
        return this;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Author active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSlug() {
        return this.slug;
    }

    public Author slug(String slug) {
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

    public Author owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Phrase getPhrase() {
        return this.phrase;
    }

    public void setPhrase(Phrase phrase) {
        if (this.phrase != null) {
            this.phrase.setAuthor(null);
        }
        if (phrase != null) {
            phrase.setAuthor(this);
        }
        this.phrase = phrase;
    }

    public Author phrase(Phrase phrase) {
        this.setPhrase(phrase);
        return this;
    }

    public Set<Ad> getAds() {
        return this.ads;
    }

    public void setAds(Set<Ad> ads) {
        if (this.ads != null) {
            this.ads.forEach(i -> i.removeAuthor(this));
        }
        if (ads != null) {
            ads.forEach(i -> i.addAuthor(this));
        }
        this.ads = ads;
    }

    public Author ads(Set<Ad> ads) {
        this.setAds(ads);
        return this;
    }

    public Author addAd(Ad ad) {
        this.ads.add(ad);
        ad.getAuthors().add(this);
        return this;
    }

    public Author removeAd(Ad ad) {
        this.ads.remove(ad);
        ad.getAuthors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Author)) {
            return false;
        }
        return getId() != null && getId().equals(((Author) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Author{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", featured='" + getFeatured() + "'" +
            ", active='" + getActive() + "'" +
            ", slug='" + getSlug() + "'" +
            "}";
    }

    public Set<Phrase> getPhrases() {
        return phrases;
    }

    public void setPhrases(Set<Phrase> phrases) {
        this.phrases = phrases;
    }
}
