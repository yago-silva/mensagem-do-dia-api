package com.mensagemdodia.domain;

import static com.mensagemdodia.domain.AuthorTestSamples.*;
import static com.mensagemdodia.domain.CategoryTestSamples.*;
import static com.mensagemdodia.domain.MediaTestSamples.*;
import static com.mensagemdodia.domain.PhraseTestSamples.*;
import static com.mensagemdodia.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PhraseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Phrase.class);
        Phrase phrase1 = getPhraseSample1();
        Phrase phrase2 = new Phrase();
        assertThat(phrase1).isNotEqualTo(phrase2);

        phrase2.setId(phrase1.getId());
        assertThat(phrase1).isEqualTo(phrase2);

        phrase2 = getPhraseSample2();
        assertThat(phrase1).isNotEqualTo(phrase2);
    }

    @Test
    void authorTest() throws Exception {
        Phrase phrase = getPhraseRandomSampleGenerator();
        Author authorBack = getAuthorRandomSampleGenerator();

        phrase.setAuthor(authorBack);
        assertThat(phrase.getAuthor()).isEqualTo(authorBack);

        phrase.author(null);
        assertThat(phrase.getAuthor()).isNull();
    }

    @Test
    void mediaTest() throws Exception {
        Phrase phrase = getPhraseRandomSampleGenerator();
        Media mediaBack = getMediaRandomSampleGenerator();

        phrase.addMedia(mediaBack);
        assertThat(phrase.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getPhrase()).isEqualTo(phrase);

        phrase.removeMedia(mediaBack);
        assertThat(phrase.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getPhrase()).isNull();

        phrase.media(new HashSet<>(Set.of(mediaBack)));
        assertThat(phrase.getMedia()).containsOnly(mediaBack);
        assertThat(mediaBack.getPhrase()).isEqualTo(phrase);

        phrase.setMedia(new HashSet<>());
        assertThat(phrase.getMedia()).doesNotContain(mediaBack);
        assertThat(mediaBack.getPhrase()).isNull();
    }

    @Test
    void categoryTest() throws Exception {
        Phrase phrase = getPhraseRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        phrase.addCategory(categoryBack);
        assertThat(phrase.getCategories()).containsOnly(categoryBack);

        phrase.removeCategory(categoryBack);
        assertThat(phrase.getCategories()).doesNotContain(categoryBack);

        phrase.categories(new HashSet<>(Set.of(categoryBack)));
        assertThat(phrase.getCategories()).containsOnly(categoryBack);

        phrase.setCategories(new HashSet<>());
        assertThat(phrase.getCategories()).doesNotContain(categoryBack);
    }

    @Test
    void tagTest() throws Exception {
        Phrase phrase = getPhraseRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        phrase.addTag(tagBack);
        assertThat(phrase.getTags()).containsOnly(tagBack);

        phrase.removeTag(tagBack);
        assertThat(phrase.getTags()).doesNotContain(tagBack);

        phrase.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(phrase.getTags()).containsOnly(tagBack);

        phrase.setTags(new HashSet<>());
        assertThat(phrase.getTags()).doesNotContain(tagBack);
    }
}
