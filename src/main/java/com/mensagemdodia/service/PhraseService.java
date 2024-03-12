package com.mensagemdodia.service;

import com.amazonaws.services.s3.AmazonS3;
import com.github.slugify.Slugify;
import com.mensagemdodia.domain.Author;
import com.mensagemdodia.domain.Media;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.domain.Tag;
import com.mensagemdodia.domain.enumeration.MediaType;
import com.mensagemdodia.repository.*;
import com.mensagemdodia.service.dto.*;
import com.mensagemdodia.service.mapper.AuthorMapper;
import com.mensagemdodia.service.mapper.CategoryMapper;
import com.mensagemdodia.service.mapper.PhraseMapper;
import com.mensagemdodia.service.mapper.TagMapper;
import java.io.*;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.compress.utils.IOUtils;
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

    private final AuthorRepository authorRepository;

    private final MediaRepository mediaRepository;

    private final AmazonS3 amazonS3;

    final Slugify slg = Slugify.builder().build();

    public PhraseService(
        PhraseRepository phraseRepository,
        PhraseMapper phraseMapper,
        AuthorMapper authorMapper,
        AuthorRepository authorRepository,
        MediaRepository mediaRepository,
        AmazonS3 amazonS3
    ) {
        this.phraseRepository = phraseRepository;
        this.phraseMapper = phraseMapper;
        this.authorMapper = authorMapper;
        this.authorRepository = authorRepository;
        this.mediaRepository = mediaRepository;
        this.amazonS3 = amazonS3;
    }

    /**
     * Save a phrase.
     *
     * @param phraseDTO the entity to save.
     * @return the persisted entity.
     */
    public PhraseDTO save(PhraseDTO phraseDTO) throws IOException {
        log.debug("Request to save Phrase : {}", phraseDTO);
        Phrase phrase = phraseMapper.toEntity(phraseDTO);
        phrase.createdAt(Instant.now());
        phrase.updatedAt(Instant.now());
        if (phrase.getSlug().isEmpty() || phrase.getSlug().length() <= 5) {
            phrase.setSlug(slg.slugify(phrase.getContent()));
        }
        phrase = phraseRepository.save(phrase);

        if (phraseDTO.getMainMediaBase64() != null) {
            String s3Key = "images/phrases/" + phrase.getId() + "/" + phrase.getSlug() + ".jpg";

            byte[] decodedBytes = Base64.getDecoder().decode(phraseDTO.getMainMediaBase64());
            File tempFile = File.createTempFile(phrase.getSlug(), ".jpg");

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
                amazonS3.putObject("mensagemdodia", s3Key, tempFile);
            } catch (Exception ex) {
                throw ex;
            }

            Media media = new Media();
            media.setPhrase(phrase);
            media.setUrl(s3Key);
            media.setActive(true);
            media.setType(MediaType.IMAGE);
            media.setHeight(720l);
            media.setWidth(720l);
            mediaRepository.save(media);

            phrase.addMedia(media);
        }
        return phraseMapper.toDto(phrase);
    }

    /**
     * Update a phrase.
     *
     * @param phraseDTO the entity to save.
     * @return the persisted entity.
     */
    public PhraseDTO update(PhraseDTO phraseDTO) throws IOException {
        log.debug("Request to update Phrase : {}", phraseDTO);
        Phrase phrase = phraseMapper.toEntity(phraseDTO);
        phrase.updatedAt(Instant.now());
        if (phrase.getSlug().isEmpty() || phrase.getSlug().length() <= 5) {
            phrase.setSlug(slg.slugify(phrase.getContent()));
        }
        phrase = phraseRepository.save(phrase);

        if (phraseDTO.getMainMediaBase64() != null) {
            String s3Key = "images/phrases/" + phrase.getId() + "/" + phrase.getSlug() + ".jpg";

            byte[] decodedBytes = Base64.getDecoder().decode(phraseDTO.getMainMediaBase64());
            File tempFile = File.createTempFile(phrase.getSlug(), ".jpg");

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(decodedBytes);
                amazonS3.putObject("mensagemdodia", s3Key, tempFile);
            } catch (Exception ex) {
                throw ex;
            }

            Media media = new Media();

            if (!phrase.getMedia().isEmpty()) {
                media = phrase.getMedia().iterator().next();
            }

            media.setPhrase(phrase);
            media.setUrl(s3Key);
            media.setActive(true);
            media.setType(MediaType.IMAGE);
            media.setHeight(720l);
            media.setWidth(720l);
            mediaRepository.save(media);
        }

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
