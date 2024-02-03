package com.mensagemdodia.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Ad.
 */
@Entity
@Table(name = "ad")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @NotNull
    @Column(name = "locale", nullable = false)
    private String locale;

    @NotNull
    @Column(name = "device_type", nullable = false)
    private String deviceType;

    @NotNull
    @Column(name = "featured", nullable = false)
    private Boolean featured;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "affiliate_link")
    private String affiliateLink;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User owner;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "ad")
    @JsonIgnoreProperties(value = { "owner", "phrase", "ad", "category", "tag" }, allowSetters = true)
    private Set<Media> media = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_ad__category", joinColumns = @JoinColumn(name = "ad_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    @JsonIgnoreProperties(value = { "owner", "parents", "media", "phrases", "ads", "tags", "category" }, allowSetters = true)
    private Set<Category> categories = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_ad__tag", joinColumns = @JoinColumn(name = "ad_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties(value = { "owner", "media", "categories", "phrases", "ads" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_ad__author", joinColumns = @JoinColumn(name = "ad_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    @JsonIgnoreProperties(value = { "owner", "phrase", "ads" }, allowSetters = true)
    private Set<Author> authors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ad id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Ad createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Ad updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getLocale() {
        return this.locale;
    }

    public Ad locale(String locale) {
        this.setLocale(locale);
        return this;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public Ad deviceType(String deviceType) {
        this.setDeviceType(deviceType);
        return this;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Boolean getFeatured() {
        return this.featured;
    }

    public Ad featured(Boolean featured) {
        this.setFeatured(featured);
        return this;
    }

    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Ad active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getAffiliateLink() {
        return this.affiliateLink;
    }

    public Ad affiliateLink(String affiliateLink) {
        this.setAffiliateLink(affiliateLink);
        return this;
    }

    public void setAffiliateLink(String affiliateLink) {
        this.affiliateLink = affiliateLink;
    }

    public User getOwner() {
        return this.owner;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    public Ad owner(User user) {
        this.setOwner(user);
        return this;
    }

    public Set<Media> getMedia() {
        return this.media;
    }

    public void setMedia(Set<Media> media) {
        if (this.media != null) {
            this.media.forEach(i -> i.setAd(null));
        }
        if (media != null) {
            media.forEach(i -> i.setAd(this));
        }
        this.media = media;
    }

    public Ad media(Set<Media> media) {
        this.setMedia(media);
        return this;
    }

    public Ad addMedia(Media media) {
        this.media.add(media);
        media.setAd(this);
        return this;
    }

    public Ad removeMedia(Media media) {
        this.media.remove(media);
        media.setAd(null);
        return this;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public Ad categories(Set<Category> categories) {
        this.setCategories(categories);
        return this;
    }

    public Ad addCategory(Category category) {
        this.categories.add(category);
        return this;
    }

    public Ad removeCategory(Category category) {
        this.categories.remove(category);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Ad tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Ad addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Ad removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Set<Author> getAuthors() {
        return this.authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Ad authors(Set<Author> authors) {
        this.setAuthors(authors);
        return this;
    }

    public Ad addAuthor(Author author) {
        this.authors.add(author);
        return this;
    }

    public Ad removeAuthor(Author author) {
        this.authors.remove(author);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ad)) {
            return false;
        }
        return getId() != null && getId().equals(((Ad) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ad{" +
            "id=" + getId() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", locale='" + getLocale() + "'" +
            ", deviceType='" + getDeviceType() + "'" +
            ", featured='" + getFeatured() + "'" +
            ", active='" + getActive() + "'" +
            ", affiliateLink='" + getAffiliateLink() + "'" +
            "}";
    }
}
