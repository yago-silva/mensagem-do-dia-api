package com.mensagemdodia.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mensagemdodia.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PhraseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PhraseDTO.class);
        PhraseDTO phraseDTO1 = new PhraseDTO();
        phraseDTO1.setId(1L);
        PhraseDTO phraseDTO2 = new PhraseDTO();
        assertThat(phraseDTO1).isNotEqualTo(phraseDTO2);
        phraseDTO2.setId(phraseDTO1.getId());
        assertThat(phraseDTO1).isEqualTo(phraseDTO2);
        phraseDTO2.setId(2L);
        assertThat(phraseDTO1).isNotEqualTo(phraseDTO2);
        phraseDTO1.setId(null);
        assertThat(phraseDTO1).isNotEqualTo(phraseDTO2);
    }
}
