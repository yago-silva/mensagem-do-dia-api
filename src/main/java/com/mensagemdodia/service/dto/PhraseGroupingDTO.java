package com.mensagemdodia.service.dto;

import java.util.ArrayList;
import java.util.List;

public class PhraseGroupingDTO {

    private String slug;
    private String name;
    private String description;
    private List<PhraseDTO> phrases;
    private List<PhraseGroupingDTO> childGroupings;

    private List<PhraseGroupingDTO> relateds;

    private CategoryDTO parentCategory;

    public PhraseGroupingDTO(
        String slug,
        String name,
        String description,
        List<PhraseDTO> phrases,
        List<PhraseGroupingDTO> childGroupings,
        List<PhraseGroupingDTO> relateds,
        CategoryDTO parentCategory
    ) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.phrases = phrases;
        this.childGroupings = childGroupings;
        this.relateds = relateds;
        this.parentCategory = parentCategory;
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

    public List<PhraseGroupingDTO> getChildGroupings() {
        return childGroupings;
    }

    public void addChildGrouping(PhraseGroupingDTO groupingDTO) {
        if (this.childGroupings == null) {
            this.childGroupings = new ArrayList<>();
        }
        this.childGroupings.add(groupingDTO);
    }

    public List<PhraseGroupingDTO> getRelateds() {
        return relateds;
    }

    public void setRelateds(List<PhraseGroupingDTO> relateds) {
        this.relateds = relateds;
    }

    public CategoryDTO getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(CategoryDTO parentCategory) {
        this.parentCategory = parentCategory;
    }
}
