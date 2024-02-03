package com.mensagemdodia.domain;

import static com.mensagemdodia.domain.AdTestSamples.*;
import static com.mensagemdodia.domain.AuthorTestSamples.*;
import static com.mensagemdodia.domain.PhraseTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class AuthorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Author.class);
        Author author1 = getAuthorSample1();
        Author author2 = new Author();
        assertThat(author1).isNotEqualTo(author2);

        author2.setId(author1.getId());
        assertThat(author1).isEqualTo(author2);

        author2 = getAuthorSample2();
        assertThat(author1).isNotEqualTo(author2);
    }

    @Test
    void phraseTest() throws Exception {
        Author author = getAuthorRandomSampleGenerator();
        Phrase phraseBack = getPhraseRandomSampleGenerator();

        author.setPhrase(phraseBack);
        assertThat(author.getPhrase()).isEqualTo(phraseBack);
        assertThat(phraseBack.getAuthor()).isEqualTo(author);

        author.phrase(null);
        assertThat(author.getPhrase()).isNull();
        assertThat(phraseBack.getAuthor()).isNull();
    }

    @Test
    void adTest() throws Exception {
        Author author = getAuthorRandomSampleGenerator();
        Ad adBack = getAdRandomSampleGenerator();

        author.addAd(adBack);
        assertThat(author.getAds()).containsOnly(adBack);
        assertThat(adBack.getAuthors()).containsOnly(author);

        author.removeAd(adBack);
        assertThat(author.getAds()).doesNotContain(adBack);
        assertThat(adBack.getAuthors()).doesNotContain(author);

        author.ads(new HashSet<>(Set.of(adBack)));
        assertThat(author.getAds()).containsOnly(adBack);
        assertThat(adBack.getAuthors()).containsOnly(author);

        author.setAds(new HashSet<>());
        assertThat(author.getAds()).doesNotContain(adBack);
        assertThat(adBack.getAuthors()).doesNotContain(author);
    }
}
