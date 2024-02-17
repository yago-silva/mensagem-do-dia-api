package com.mensagemdodia.service.dto;

import java.util.List;

public class AuthorPhrasesDTO {

    private AuthorDTO author;

    private List<PhraseDTO> phrases;

    public AuthorPhrasesDTO(AuthorDTO author, List<PhraseDTO> phrases) {
        this.author = author;
        this.phrases = phrases;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public List<PhraseDTO> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<PhraseDTO> phrases) {
        this.phrases = phrases;
    }
}
