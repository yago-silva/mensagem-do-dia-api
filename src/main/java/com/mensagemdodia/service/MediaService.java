package com.mensagemdodia.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.mensagemdodia.domain.Category;
import com.mensagemdodia.domain.ImageMediaEditor;
import com.mensagemdodia.domain.Media;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.repository.CategoryRepository;
import com.mensagemdodia.repository.MediaRepository;
import com.mensagemdodia.repository.PhraseRepository;
import com.mensagemdodia.service.dto.CreateImageMediaDTO;
import com.mensagemdodia.service.dto.MediaDTO;
import com.mensagemdodia.service.mapper.MediaMapper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mensagemdodia.domain.Media}.
 */
@Service
@Transactional
public class MediaService {

    private final Logger log = LoggerFactory.getLogger(MediaService.class);

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    private final PhraseRepository phraseRepository;

    private final CategoryRepository categoryRepository;

    private final AmazonS3 amazonS3;

    public MediaService(
        MediaRepository mediaRepository,
        MediaMapper mediaMapper,
        PhraseRepository phraseRepository,
        CategoryRepository categoryRepository,
        AmazonS3 amazonS3
    ) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
        this.phraseRepository = phraseRepository;
        this.categoryRepository = categoryRepository;
        this.amazonS3 = amazonS3;
    }

    /**
     * Save a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaDTO save(MediaDTO mediaDTO) {
        log.debug("Request to save Media : {}", mediaDTO);
        Media media = mediaMapper.toEntity(mediaDTO);
        media.createdAt(Instant.now());
        media.updatedAt(Instant.now());
        media = mediaRepository.save(media);
        return mediaMapper.toDto(media);
    }

    /**
     * Update a media.
     *
     * @param mediaDTO the entity to save.
     * @return the persisted entity.
     */
    public MediaDTO update(MediaDTO mediaDTO) {
        log.debug("Request to update Media : {}", mediaDTO);
        Media media = mediaMapper.toEntity(mediaDTO);
        media.updatedAt(Instant.now());
        media = mediaRepository.save(media);
        return mediaMapper.toDto(media);
    }

    /**
     * Partially update a media.
     *
     * @param mediaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MediaDTO> partialUpdate(MediaDTO mediaDTO) {
        log.debug("Request to partially update Media : {}", mediaDTO);

        return mediaRepository
            .findById(mediaDTO.getId())
            .map(existingMedia -> {
                existingMedia.updatedAt(Instant.now());
                mediaMapper.partialUpdate(existingMedia, mediaDTO);

                return existingMedia;
            })
            .map(mediaRepository::save)
            .map(mediaMapper::toDto);
    }

    /**
     * Get all the media.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MediaDTO> findAll() {
        log.debug("Request to get all Media");
        return mediaRepository.findAll().stream().map(mediaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one media by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MediaDTO> findOne(Long id) {
        log.debug("Request to get Media : {}", id);
        return mediaRepository.findById(id).map(mediaMapper::toDto);
    }

    /**
     * Delete the media by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Media : {}", id);
        mediaRepository.deleteById(id);
    }

    public byte[] createNewImageForPhrase(CreateImageMediaDTO createImageMediaDTO) throws IOException {
        //Phrase phrase = phraseRepository.findById(phraseId).orElseThrow();
        List<Category> categories = categoryRepository.getAllByIds(createImageMediaDTO.getCategoryIds());

        List<String> suggestedImages = new ArrayList<>();

        if (!categories.isEmpty()) {
            suggestedImages = categories
                .stream()
                .map(category -> {
                    ObjectListing objectListing = amazonS3.listObjects("mensagemdodia", "images/suggestions/" + category.getSlug());
                    return objectListing.getObjectSummaries();
                })
                .flatMap(Collection::stream)
                .map(S3ObjectSummary::getKey)
                .filter(key -> key.endsWith(".jpg") || key.endsWith(".jpeg"))
                .map(key -> "https://mensagemdodia.s3.sa-east-1.amazonaws.com/" + key)
                .collect(Collectors.toList());
        }

        if (suggestedImages.isEmpty()) {
            suggestedImages.add("https://mensagemdodia.s3.sa-east-1.amazonaws.com/images/suggestions/general/general-1.jpg");
            suggestedImages.add("https://mensagemdodia.s3.sa-east-1.amazonaws.com/images/suggestions/general/general-2.jpg");
            suggestedImages.add("https://mensagemdodia.s3.sa-east-1.amazonaws.com/images/suggestions/general/general-3.jpg");
        }

        //        phrase.getCategories().stream().forEach();

        Random rand = new Random();

        BufferedImage image = ImageIO.read(
            //new URL("https://mensagemdodia.s3.sa-east-1.amazonaws.com/testes-com-imagens/frases_de_amor.jpg")
            new URL(suggestedImages.get(rand.nextInt(suggestedImages.size())))
        );

        return ImageMediaEditor.addPhraseToImage(image, createImageMediaDTO.getMainText(), createImageMediaDTO.getSecondaryText());
    }
}
