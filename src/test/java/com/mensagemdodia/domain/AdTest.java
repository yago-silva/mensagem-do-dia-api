package com.mensagemdodia.domain;

import static com.mensagemdodia.domain.AdTestSamples.*;
import static com.mensagemdodia.domain.AuthorTestSamples.*;
import static com.mensagemdodia.domain.CategoryTestSamples.*;
import static com.mensagemdodia.domain.MediaTestSamples.*;
import static com.mensagemdodia.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AdTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ad.class);
        Ad ad1 = getAdSample1();
        Ad ad2 = new Ad();
        assertThat(ad1).isNotEqualTo(ad2);

        ad2.setId(ad1.getId());
        assertThat(ad1).isEqualTo(ad2);

        ad2 = getAdSample2();
        assertThat(ad1).isNotEqualTo(ad2);
    }

    @Test
    void mediaTest() throws Exception {
        Ad ad = getAdRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        ad.addMedia(mediaBack);
        assertThat(ad.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getAd()).isEqualTo(ad);

        ad.removeMedia(mediaBack);
        assertThat(ad.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getAd()).isNull();

        ad.media(new HashSet<>(Set.of(mediaBack)));
        assertThat(ad.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getAd()).isEqualTo(ad);

        ad.setMedia(new HashSet<>());
        assertThat(ad.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getAd()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Ad ad = getAdRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        ad.addCategory(categoryBack);
        assertThat(ad.getCategories()).containsOnly(categoryBack);

        ad.removeCategory(categoryBack);
        assertThat(ad.getCategories()).doesNotContain(categoryBack);

        ad.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(ad.getCategories()).containsOnly(categoryBack);

        ad.setCategories(new HashSet<>());
        assertThat(ad.getCategories()).doesNotContain(categoryBack);
    }

    @Test
    void tagTest() throws Exception {
        Ad ad = getAdRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        ad.addTag(tagBack);
        assertThat(ad.getTags()).containsOnly(tagBack);

        ad.removeTag(tagBack);
        assertThat(ad.getTags()).doesNotContain(tagBack);

        ad.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(ad.getTags()).containsOnly(tagBack);

        ad.setTags(new HashSet<>());
        assertThat(ad.getTags()).doesNotContain(tagBack);
    }

    @Test
    void authorTest() throws Exception {
        Ad ad = getAdRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        ad.addAuthor(authorBack);
        assertThat(ad.getAuthors()).containsOnly(authorBack);

        ad.removeAuthor(authorBack);
        assertThat(ad.getAuthors()).doesNotContain(authorBack);

        ad.authors(new HashSet<>(Set.of(authorBack)));
        assertThat(ad.getAuthors()).containsOnly(authorBack);

        ad.setAuthors(new HashSet<>());
        assertThat(ad.getAuthors()).doesNotContain(authorBack);
    }
}
