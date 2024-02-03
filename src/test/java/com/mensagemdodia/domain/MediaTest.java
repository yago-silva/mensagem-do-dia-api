package com.mensagemdodia.domain;

import static com.mensagemdodia.domain.AdTestSamples.*;
import static com.mensagemdodia.domain.CategoryTestSamples.*;
import static com.mensagemdodia.domain.MediaTestSamples.*;
import static com.mensagemdodia.domain.PhraseTestSamples.*;
import static com.mensagemdodia.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MediaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Media.class);
        Media media1 = getMediaSample1();
        Media media2 = new Media();
        assertThat(media1).isNotEqualTo(media2);

        media2.setId(media1.getId());
        assertThat(media1).isEqualTo(media2);

        media2 = getMediaSample2();
        assertThat(media1).isNotEqualTo(media2);
    }

    @Test
    void phraseTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Phrase phraseBack = getPhraseRandomSampleGenerator();

        media.setPhrase(phraseBack);
        assertThat(media.getPhrase()).isEqualTo(phraseBack);

        media.phrase(null);
        assertThat(media.getPhrase()).isNull();
    }

    @Test
    void adTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Ad adBack = getAdRandomSampleGenerator();

        media.setAd(adBack);
        assertThat(media.getAd()).isEqualTo(adBack);

        media.ad(null);
        assertThat(media.getAd()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        media.setCategory(categoryBack);
        assertThat(media.getCategory()).isEqualTo(categoryBack);

        media.category(null);
        assertThat(media.getCategory()).isNull();
    }

    @Test
    void tagTest() throws Exception {
        Media media = getMediaRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        media.setTag(tagBack);
        assertThat(media.getTag()).isEqualTo(tagBack);

        media.tag(null);
        assertThat(media.getTag()).isNull();
    }
}
