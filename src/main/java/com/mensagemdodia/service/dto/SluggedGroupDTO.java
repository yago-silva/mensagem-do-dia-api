package com.mensagemdodia.service.dto;

import java.util.List;

public class SluggedGroupDTO {

    private CategoryDTO category;

    private TagDTO tag;

    private List<PhraseDTO> phrases;

    public SluggedGroupDTO(CategoryDTO category, TagDTO tag, List<PhraseDTO> phrases) {
        this.category = category;
        this.tag = tag;
        this.phrases = phrases;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public TagDTO getTag() {
        return tag;
    }

    public void setTag(TagDTO tag) {
        this.tag = tag;
    }

    public List<PhraseDTO> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<PhraseDTO> phrases) {
        this.phrases = phrases;
    }
}
