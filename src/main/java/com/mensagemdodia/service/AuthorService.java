package com.mensagemdodia.service;

import com.github.slugify.Slugify;
import com.mensagemdodia.domain.Author;
import com.mensagemdodia.repository.AuthorRepository;
import com.mensagemdodia.service.dto.AuthorDTO;
import com.mensagemdodia.service.dto.CategoryDTO;
import com.mensagemdodia.service.mapper.AuthorMapper;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mensagemdodia.domain.Author}.
 */
@Service
@Transactional
public class AuthorService {

    private final Logger log = LoggerFactory.getLogger(AuthorService.class);

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    final Slugify slg = Slugify.builder().build();

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Save a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO save(AuthorDTO authorDTO) {
        log.debug("Request to save Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author.createdAt(Instant.now());
        author.updatedAt(Instant.now());

        if (author.getSlug().isEmpty() || author.getSlug().length() <= 5) {
            author.setSlug(slg.slugify(author.getName()));
        }
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Update a author.
     *
     * @param authorDTO the entity to save.
     * @return the persisted entity.
     */
    public AuthorDTO update(AuthorDTO authorDTO) {
        log.debug("Request to update Author : {}", authorDTO);
        Author author = authorMapper.toEntity(authorDTO);
        author.updatedAt(Instant.now());
        if (author.getSlug().isEmpty() || author.getSlug().length() <= 5) {
            author.setSlug(slg.slugify(author.getName()));
        }
        author = authorRepository.save(author);
        return authorMapper.toDto(author);
    }

    /**
     * Partially update a author.
     *
     * @param authorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AuthorDTO> partialUpdate(AuthorDTO authorDTO) {
        log.debug("Request to partially update Author : {}", authorDTO);

        return authorRepository
            .findById(authorDTO.getId())
            .map(existingAuthor -> {
                existingAuthor.updatedAt(Instant.now());
                authorMapper.partialUpdate(existingAuthor, authorDTO);

                return existingAuthor;
            })
            .map(authorRepository::save)
            .map(authorMapper::toDto);
    }

    /**
     * Get all the authors.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAll() {
        log.debug("Request to get all Authors");
        return authorRepository.findAll().stream().map(authorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<AuthorDTO> findAllActive() {
        log.debug("Request to get all active Authors");
        return authorRepository.getAllActive().stream().map(authorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the authors where Phrase is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AuthorDTO> findAllWherePhraseIsNull() {
        log.debug("Request to get all authors where Phrase is null");
        return StreamSupport.stream(authorRepository.findAll().spliterator(), false)
            .map(authorMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<AuthorDTO> findAllFeatured() {
        log.debug("Request to get all featured Authors");
        return authorRepository.getAllFeatured().stream().map(authorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one author by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AuthorDTO> findOne(Long id) {
        log.debug("Request to get Author : {}", id);
        return authorRepository.findById(id).map(authorMapper::toDto);
    }

    /**
     * Delete the author by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Author : {}", id);
        authorRepository.deleteById(id);
    }
}
