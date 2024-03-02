package com.mensagemdodia.domain;

import static com.mensagemdodia.domain.AdTestSamples.*;
import static com.mensagemdodia.domain.CategoryTestSamples.*;
import static com.mensagemdodia.domain.MediaTestSamples.*;
import static com.mensagemdodia.domain.PhraseTestSamples.*;
import static com.mensagemdodia.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Category.class);
        Category category1 = getCategorySample1();
        Category category2 = new Category();
        assertThat(category1).isNotEqualTo(category2);

        category2.setId(category1.getId());
        assertThat(category1).isEqualTo(category2);

        category2 = getCategorySample2();
        assertThat(category1).isNotEqualTo(category2);
    }

    @Test
    void parentTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.addParent(categoryBack);
        assertThat(category.getChildCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getCategory()).isEqualTo(category);

        category.removeParent(categoryBack);
        assertThat(category.getChildCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getCategory()).isNull();

        category.parents(new HashSet<>(Set.of(categoryBack)));
        assertThat(category.getChildCategories()).containsOnly(categoryBack);
        assertThat(categoryBack.getCategory()).isEqualTo(category);

        category.setChildCategories(new HashSet<>());
        assertThat(category.getChildCategories()).doesNotContain(categoryBack);
        assertThat(categoryBack.getCategory()).isNull();
    }

    @Test
    void mediaTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        category.addMedia(mediaBack);
        assertThat(category.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getCategory()).isEqualTo(category);

        category.removeMedia(mediaBack);
        assertThat(category.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getCategory()).isNull();

        category.media(new HashSet<>(Set.of(mediaBack)));
        assertThat(category.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getCategory()).isEqualTo(category);

        category.setMedia(new HashSet<>());
        assertThat(category.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getCategory()).isNull();
    }

    @Test
    void phraseTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Phrase phraseBack = getPhraseRandomSampleGenerator();

        category.addPhrase(phraseBack);
        assertThat(category.getPhrases()).containsOnly(phraseBack);
        assertThat(phraseBack.getCategories()).containsOnly(category);

        category.removePhrase(phraseBack);
        assertThat(category.getPhrases()).doesNotContain(phraseBack);
        assertThat(phraseBack.getCategories()).doesNotContain(category);

        category.phrases(new HashSet<>(Set.of(phraseBack)));
        assertThat(category.getPhrases()).containsOnly(phraseBack);
        assertThat(phraseBack.getCategories()).containsOnly(category);

        category.setPhrases(new HashSet<>());
        assertThat(category.getPhrases()).doesNotContain(phraseBack);
        assertThat(phraseBack.getCategories()).doesNotContain(category);
    }

    @Test
    void adTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Ad adBack = getAdRandomSampleGenerator();

        category.addAd(adBack);
        assertThat(category.getAds()).containsOnly(adBack);
        assertThat(adBack.getCategories()).containsOnly(category);

        category.removeAd(adBack);
        assertThat(category.getAds()).doesNotContain(adBack);
        assertThat(adBack.getCategories()).doesNotContain(category);

        category.ads(new HashSet<>(Set.of(adBack)));
        assertThat(category.getAds()).containsOnly(adBack);
        assertThat(adBack.getCategories()).containsOnly(category);

        category.setAds(new HashSet<>());
        assertThat(category.getAds()).doesNotContain(adBack);
        assertThat(adBack.getCategories()).doesNotContain(category);
    }

    @Test
    void tagTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        category.addTag(tagBack);
        assertThat(category.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getCategories()).containsOnly(category);

        category.removeTag(tagBack);
        assertThat(category.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getCategories()).doesNotContain(category);

        category.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(category.getTags()).containsOnly(tagBack);
        assertThat(tagBack.getCategories()).containsOnly(category);

        category.setTags(new HashSet<>());
        assertThat(category.getTags()).doesNotContain(tagBack);
        assertThat(tagBack.getCategories()).doesNotContain(category);
    }

    @Test
    void categoryTest() throws Exception {
        Category category = getCategoryRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        category.setCategory(categoryBack);
        assertThat(category.getCategory()).isEqualTo(categoryBack);

        category.category(null);
        assertThat(category.getCategory()).isNull();
    }
}
