package com.mensagemdodia.web.rest;

import com.mensagemdodia.repository.AdRepository;
import com.mensagemdodia.service.AdService;
import com.mensagemdodia.service.dto.AdDTO;
import com.mensagemdodia.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mensagemdodia.domain.Ad}.
 */
@RestController
@RequestMapping("/api/ads")
public class AdResource {

    private final Logger log = LoggerFactory.getLogger(AdResource.class);

    private static final String ENTITY_NAME = "ad";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdService adService;

    private final AdRepository adRepository;

    public AdResource(AdService adService, AdRepository adRepository) {
        this.adService = adService;
        this.adRepository = adRepository;
    }

    /**
     * {@code POST  /ads} : Create a new ad.
     *
     * @param adDTO the adDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adDTO, or with status {@code 400 (Bad Request)} if the ad has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdDTO> createAd(@Valid @RequestBody AdDTO adDTO) throws URISyntaxException {
        log.debug("REST request to save Ad : {}", adDTO);
        if (adDTO.getId() != null) {
            throw new BadRequestAlertException("A new ad cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdDTO result = adService.save(adDTO);
        return ResponseEntity
            .created(new URI("/api/ads/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ads/:id} : Updates an existing ad.
     *
     * @param id the id of the adDTO to save.
     * @param adDTO the adDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adDTO,
     * or with status {@code 400 (Bad Request)} if the adDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdDTO> updateAd(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody AdDTO adDTO)
        throws URISyntaxException {
        log.debug("REST request to update Ad : {}, {}", id, adDTO);
        if (adDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdDTO result = adService.update(adDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ads/:id} : Partial updates given fields of an existing ad, field will ignore if it is null
     *
     * @param id the id of the adDTO to save.
     * @param adDTO the adDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adDTO,
     * or with status {@code 400 (Bad Request)} if the adDTO is not valid,
     * or with status {@code 404 (Not Found)} if the adDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the adDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdDTO> partialUpdateAd(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdDTO adDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ad partially : {}, {}", id, adDTO);
        if (adDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, adDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!adRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdDTO> result = adService.partialUpdate(adDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ads} : get all the ads.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ads in body.
     */
    @GetMapping("")
    public List<AdDTO> getAllAds(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Ads");
        return adService.findAll();
    }

    /**
     * {@code GET  /ads/:id} : get the "id" ad.
     *
     * @param id the id of the adDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdDTO> getAd(@PathVariable("id") Long id) {
        log.debug("REST request to get Ad : {}", id);
        Optional<AdDTO> adDTO = adService.findOne(id);
        return ResponseUtil.wrapOrNotFound(adDTO);
    }

    /**
     * {@code DELETE  /ads/:id} : delete the "id" ad.
     *
     * @param id the id of the adDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable("id") Long id) {
        log.debug("REST request to delete Ad : {}", id);
        adService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
