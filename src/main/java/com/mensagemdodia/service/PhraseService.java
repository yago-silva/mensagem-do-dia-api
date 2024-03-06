package com.mensagemdodia.service;

import com.mensagemdodia.domain.Author;
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
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mensagemdodia.domain.Phrase}.
 */
@Service
@Transactional
public class PhraseService {

    private final Logger log = LoggerFactory.getLogger(PhraseService.class);

    private final PhraseRepository phraseRepository;

    private final PhraseMapper phraseMapper;

    private final AuthorMapper authorMapper;

    private final CategoryMapper categoryMapper;

    private final TagMapper tagMapper;

    private final AuthorRepository authorRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    public PhraseService(
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

    /**
     * Save a phrase.
     *
     * @param phraseDTO the entity to save.
     * @return the persisted entity.
     */
    public PhraseDTO save(PhraseDTO phraseDTO) {
        log.debug("Request to save Phrase : {}", phraseDTO);
        Phrase phrase = phraseMapper.toEntity(phraseDTO);
        phrase.createdAt(Instant.now());
        phrase.updatedAt(Instant.now());
        phrase = phraseRepository.save(phrase);
        return phraseMapper.toDto(phrase);
    }

    /**
     * Update a phrase.
     *
     * @param phraseDTO the entity to save.
     * @return the persisted entity.
     */
    public PhraseDTO update(PhraseDTO phraseDTO) {
        log.debug("Request to update Phrase : {}", phraseDTO);
        Phrase phrase = phraseMapper.toEntity(phraseDTO);
        phrase.updatedAt(Instant.now());
        phrase = phraseRepository.save(phrase);
        return phraseMapper.toDto(phrase);
    }

    /**
     * Partially update a phrase.
     *
     * @param phraseDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PhraseDTO> partialUpdate(PhraseDTO phraseDTO) {
        log.debug("Request to partially update Phrase : {}", phraseDTO);

        return phraseRepository
            .findById(phraseDTO.getId())
            .map(existingPhrase -> {
                existingPhrase.updatedAt(Instant.now());
                phraseMapper.partialUpdate(existingPhrase, phraseDTO);

                return existingPhrase;
            })
            .map(phraseRepository::save)
            .map(phraseMapper::toDto);
    }

    /**
     * Get all the phrases.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PhraseDTO> findAll() {
        log.debug("Request to get all Phrases");
        return phraseRepository.findAll().stream().map(phraseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<AuthorPhrasesDTO> getAllByAuthorSlug(String slug, boolean includeInactives) {
        log.debug("Request to get all Phrases by author slug: " + slug);
        Optional<Author> optionalAuthor = authorRepository.findBySlug(slug, includeInactives);

        if (!optionalAuthor.isPresent()) {
            return Optional.empty();
        }

        Author author = optionalAuthor.orElseThrow();

        List<Phrase> phrases = phraseRepository.findAllByAuthorSlug(slug, includeInactives);

        return Optional.of(new AuthorPhrasesDTO(authorMapper.toDto(author), phrases.stream().map(phraseMapper::toDto).toList()));
    }

    /**
     * Get all the phrases with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PhraseDTO> findAllWithEagerRelationships(Pageable pageable) {
        return phraseRepository.findAllWithEagerRelationships(pageable).map(phraseMapper::toDto);
    }

    /**
     * Get one phrase by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PhraseDTO> findOne(Long id) {
        log.debug("Request to get Phrase : {}", id);
        return phraseRepository.findOneWithEagerRelationships(id).map(phraseMapper::toDto);
    }

    /**
     * Delete the phrase by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Phrase : {}", id);
        phraseRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PhraseDTO> findAllFeatured() {
        log.debug("Request to get all featured Tags");
        return phraseRepository.getAllFeatured().stream().map(phraseMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
