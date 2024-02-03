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

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void mediaTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        tag.addMedia(mediaBack);
        assertThat(tag.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getTag()).isEqualTo(tag);

        tag.removeMedia(mediaBack);
        assertThat(tag.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getTag()).isNull();

        tag.media(new HashSet<>(Set.of(mediaBack)));
        assertThat(tag.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getTag()).isEqualTo(tag);

        tag.setMedia(new HashSet<>());
        assertThat(tag.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getTag()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        tag.addCategory(categoryBack);
        assertThat(tag.getCategories()).containsOnly(categoryBack);

        tag.removeCategory(categoryBack);
        assertThat(tag.getCategories()).doesNotContain(categoryBack);

        tag.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(tag.getCategories()).containsOnly(categoryBack);

        tag.setCategories(new HashSet<>());
        assertThat(tag.getCategories()).doesNotContain(categoryBack);
    }

    @Test
    void phraseTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Phrase phraseBack = getPhraseRandomSampleGenerator();

        tag.addPhrase(phraseBack);
        assertThat(tag.getPhrases()).containsOnly(phraseBack);
        assertThat(phraseBack.getTags()).containsOnly(tag);

        tag.removePhrase(phraseBack);
        assertThat(tag.getPhrases()).doesNotContain(phraseBack);
        assertThat(phraseBack.getTags()).doesNotContain(tag);

        tag.phrases(new HashSet<>(Set.of(phraseBack)));
        assertThat(tag.getPhrases()).containsOnly(phraseBack);
        assertThat(phraseBack.getTags()).containsOnly(tag);

        tag.setPhrases(new HashSet<>());
        assertThat(tag.getPhrases()).doesNotContain(phraseBack);
        assertThat(phraseBack.getTags()).doesNotContain(tag);
    }

    @Test
    void adTest() throws Exception {
        Tag tag = getTagRandomSampleGenerator();
        Ad adBack = getAdRandomSampleGenerator();

        tag.addAd(adBack);
        assertThat(tag.getAds()).containsOnly(adBack);
        assertThat(adBack.getTags()).containsOnly(tag);

        tag.removeAd(adBack);
        assertThat(tag.getAds()).doesNotContain(adBack);
        assertThat(adBack.getTags()).doesNotContain(tag);

        tag.ads(new HashSet<>(Set.of(adBack)));
        assertThat(tag.getAds()).containsOnly(adBack);
        assertThat(adBack.getTags()).containsOnly(tag);

        tag.setAds(new HashSet<>());
        assertThat(tag.getAds()).doesNotContain(adBack);
        assertThat(adBack.getTags()).doesNotContain(tag);
    }
}
