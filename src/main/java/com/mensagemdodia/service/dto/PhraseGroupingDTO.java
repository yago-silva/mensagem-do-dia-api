package com.mensagemdodia.service.dto;

import java.util.ArrayList;
import java.util.List;

public class PhraseGroupingDTO {

    private String slug;
    private String name;
    private String description;
    private List<PhraseDTO> phrases;
    private List<PhraseGroupingDTO> childGrouping;

    public PhraseGroupingDTO(String slug, String name, String description, List<PhraseDTO> phrases, List<PhraseGroupingDTO> childGrouping) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.phrases = phrases;
        this.childGrouping = childGrouping;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<PhraseDTO> getPhrases() {
        return phrases;
    }

    public void setPhrases(List<PhraseDTO> phrases) {
        this.phrases = phrases;
    }

    public List<PhraseGroupingDTO> getChildGrouping() {
        return childGrouping;
    }

    public void addChildGrouping(PhraseGroupingDTO groupingDTO) {
        if (this.childGrouping == null) {
            this.childGrouping = new ArrayList<>();
        }
        this.childGrouping.add(groupingDTO);
    }
}
