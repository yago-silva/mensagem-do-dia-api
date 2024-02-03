package com.mensagemdodia.service;

import com.mensagemdodia.domain.Ad;
import com.mensagemdodia.repository.AdRepository;
import com.mensagemdodia.service.dto.AdDTO;
import com.mensagemdodia.service.mapper.AdMapper;
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
 * Service Implementation for managing {@link com.mensagemdodia.domain.Ad}.
 */
@Service
@Transactional
public class AdService {

    private final Logger log = LoggerFactory.getLogger(AdService.class);

    private final AdRepository adRepository;

    private final AdMapper adMapper;

    public AdService(AdRepository adRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.adMapper = adMapper;
    }

    /**
     * Save a ad.
     *
     * @param adDTO the entity to save.
     * @return the persisted entity.
     */
    public AdDTO save(AdDTO adDTO) {
        log.debug("Request to save Ad : {}", adDTO);
        Ad ad = adMapper.toEntity(adDTO);
        ad = adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    /**
     * Update a ad.
     *
     * @param adDTO the entity to save.
     * @return the persisted entity.
     */
    public AdDTO update(AdDTO adDTO) {
        log.debug("Request to update Ad : {}", adDTO);
        Ad ad = adMapper.toEntity(adDTO);
        ad = adRepository.save(ad);
        return adMapper.toDto(ad);
    }

    /**
     * Partially update a ad.
     *
     * @param adDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdDTO> partialUpdate(AdDTO adDTO) {
        log.debug("Request to partially update Ad : {}", adDTO);

        return adRepository
            .findById(adDTO.getId())
            .map(existingAd -> {
                adMapper.partialUpdate(existingAd, adDTO);

                return existingAd;
            })
            .map(adRepository::save)
            .map(adMapper::toDto);
    }

    /**
     * Get all the ads.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AdDTO> findAll() {
        log.debug("Request to get all Ads");
        return adRepository.findAll().stream().map(adMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the ads with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AdDTO> findAllWithEagerRelationships(Pageable pageable) {
        return adRepository.findAllWithEagerRelationships(pageable).map(adMapper::toDto);
    }

    /**
     * Get one ad by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdDTO> findOne(Long id) {
        log.debug("Request to get Ad : {}", id);
        return adRepository.findOneWithEagerRelationships(id).map(adMapper::toDto);
    }

    /**
     * Delete the ad by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ad : {}", id);
        adRepository.deleteById(id);
    }
}
