package com.mensagemdodia.service;

import com.mensagemdodia.domain.ImageMediaEditor;
import com.mensagemdodia.domain.Media;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.repository.MediaRepository;
import com.mensagemdodia.repository.PhraseRepository;
import com.mensagemdodia.service.dto.MediaDTO;
import com.mensagemdodia.service.mapper.MediaMapper;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public MediaService(MediaRepository mediaRepository, MediaMapper mediaMapper, PhraseRepository phraseRepository) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
        this.phraseRepository = phraseRepository;
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

    public byte[] createNewImageForPhrase(Long phraseId) throws IOException {
        Phrase phrase = phraseRepository.findById(phraseId).orElseThrow();

        //        Set<String> suggestedImages =

        //        phrase.getCategories().stream().forEach();

        BufferedImage image = ImageIO.read(
            new URL("https://mensagemdodia.s3.sa-east-1.amazonaws.com/testes-com-imagens/frases_de_amor.jpg")
        );

        return ImageMediaEditor.addPhraseToImage(image, phrase);
    }
}
