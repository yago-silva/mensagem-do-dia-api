package com.mensagemdodia.service;

import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.Tag;
import com.mensagemdodia.repository.AuthorRepository;
import com.mensagemdodia.repository.CategoryRepository;
import com.mensagemdodia.repository.PhraseRepository;
import com.mensagemdodia.repository.TagRepository;
import com.mensagemdodia.service.dto.*;
import com.mensagemdodia.service.mapper.AuthorMapper;
import com.mensagemdodia.service.mapper.CategoryMapper;
import com.mensagemdodia.service.mapper.PhraseMapper;
import com.mensagemdodia.service.mapper.TagMapper;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Phrase}.
 */
@Service
@Transactional
public class PhraseGroupingService {

    private final Logger log = LoggerFactory.getLogger(PhraseGroupingService.class);

    private final PhraseRepository phraseRepository;

    private final PhraseMapper phraseMapper;

    private final AuthorMapper authorMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    public PhraseGroupingService(
        PhraseRepository phraseRepository,
        PhraseMapper phraseMapper,
        AuthorMapper authorMapper,
        CategoryMapper categoryMapper,
        TagMapper tagMapper,
        AuthorRepository authorRepository,
        CategoryRepository categoryRepository,
        TagRepository tagRepository
    ) {
        this.phraseRepository = phraseRepository;
        this.phraseMapper = phraseMapper;
        this.authorMapper = authorMapper;
        this.categoryMapper = categoryMapper;
        this.tagMapper = tagMapper;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @Transactional(readOnly = true)
    public Optional<PhraseGroupingDTO> getPhrasesGroupingSlug(String groupingSlug, boolean includeChildGroupings) {
        log.debug("Request to get Phrases of grouping: " + groupingSlug);

        Optional<Category> optionalCategory = categoryRepository.findBySlug(groupingSlug);
        Optional<Tag> optionalTag = tagRepository.findBySlug(groupingSlug);

        if (optionalCategory.isEmpty() && optionalTag.isEmpty()) {
            return Optional.empty();
        }

        final PhraseGroupingDTO phraseGroupingDTO = buildPhraseGrouping(
            groupingSlug,
            optionalCategory.map(categoryMapper::toDto),
            optionalTag.map(tagMapper::toDto),
            true
        );

        var alreadyUsedPhrases = new HashSet<Long>();

        var relatedGroupings = new ArrayList<PhraseGroupingDTO>();

        if (optionalCategory.isPresent() && includeChildGroupings) {
            Category category = optionalCategory.get();
            List<Category> childCategories = categoryRepository.findChildCategoryByParentCategoryId(category.getId());

            //Child Categories
            childCategories.forEach(childCategory -> {
                var childCategoryPhraseGroup = buildPhraseGrouping(
                    childCategory.getSlug(),
                    Optional.of(childCategory).map(categoryMapper::toDto),
                    Optional.empty(),
                    true
                );

                childCategoryPhraseGroup.setPhrases(
                    childCategoryPhraseGroup.getPhrases().stream().filter(phrase -> !alreadyUsedPhrases.contains(phrase.getId())).toList()
                );

                phraseGroupingDTO.addChildGrouping(childCategoryPhraseGroup);

                alreadyUsedPhrases.addAll(childCategory.getPhrases().stream().map(Phrase::getId).collect(Collectors.toSet()));
            });

            //Add the related parent category
            if (category.getCategory() != null) {
                Category parentCategory = category.getCategory();
                relatedGroupings.add(
                    buildPhraseGrouping(
                        parentCategory.getSlug(),
                        Optional.of(parentCategory).map(categoryMapper::toDto),
                        Optional.empty(),
                        false
                    )
                );

                phraseGroupingDTO.setParentCategory(Optional.of(parentCategory).map(categoryMapper::toDto).get());
            }

            //Add category related tags
            tagRepository
                .getAllRelatedWithCategories(category.getId())
                .stream()
                .map(tagMapper::toDto)
                .map(tagDto -> buildPhraseGrouping(tagDto.getSlug(), Optional.empty(), Optional.of(tagDto), false))
                .forEach(relatedGroupings::add);
        } else if (optionalTag.isPresent()) {
            Tag tag = optionalTag.get();
            //Add tag related categories
            tag
                .getCategories()
                .stream()
                .map(categoryMapper::toDto)
                .map(categoryDto -> buildPhraseGrouping(categoryDto.getSlug(), Optional.of(categoryDto), Optional.empty(), false))
                .forEach(relatedGroupings::add);
        }

        phraseGroupingDTO.setPhrases(
            phraseGroupingDTO.getPhrases().stream().filter(phrase -> !alreadyUsedPhrases.contains(phrase.getId())).toList()
        );

        phraseGroupingDTO.setRelateds(relatedGroupings);

        return Optional.of(phraseGroupingDTO);
    }

    private PhraseGroupingDTO buildPhraseGrouping(
        String groupingSlug,
        Optional<CategoryDTO> optionalCategoryDTO,
        Optional<TagDTO> optionalTagDTO,
        boolean includePhrases
    ) {
        LinkedList<PhraseDTO> phrases = new LinkedList<>();

        if (includePhrases) {
            phrases = phraseRepository
                .findAllByGroupSlug(groupingSlug)
                .stream()
                .map(phraseMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
        }
        var groupingName = optionalCategoryDTO
            .map(categoryDTO -> categoryDTO.getName())
            .orElseGet(() -> optionalTagDTO.map(tagDTO -> tagDTO.getName()).orElseThrow());

        var groupingDescription = optionalCategoryDTO
            .map(categoryDTO -> categoryDTO.getDescription())
            .orElseGet(() -> optionalTagDTO.map(tagDTO -> tagDTO.getDescription()).orElse(null));

        var childGroupings = new ArrayList<PhraseGroupingDTO>();

        return new PhraseGroupingDTO(groupingSlug, groupingName, groupingDescription, phrases, childGroupings, new ArrayList<>(), null);
    }
}
