package com.mensagemdodia.web.rest;

import com.mensagemdodia.gateway.InstagramClient;
import com.mensagemdodia.repository.PhraseRepository;
import com.mensagemdodia.service.PhraseService;
import com.mensagemdodia.service.dto.AuthorPhrasesDTO;
import com.mensagemdodia.service.dto.PhraseDTO;
import com.mensagemdodia.service.dto.SluggedGroupDTO;
import com.mensagemdodia.service.dto.TagDTO;
import com.mensagemdodia.web.rest.errors.BadRequestAlertException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
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
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mensagemdodia.domain.Phrase}.
 */
@RestController
@RequestMapping("/api/phrases")
public class PhraseResource {

    private final Logger log = LoggerFactory.getLogger(PhraseResource.class);

    private static final String ENTITY_NAME = "phrase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PhraseService phraseService;

    private final PhraseRepository phraseRepository;

    public PhraseResource(PhraseService phraseService, PhraseRepository phraseRepository) {
        this.phraseService = phraseService;
        this.phraseRepository = phraseRepository;
    }

    /**
     * {@code POST  /phrases} : Create a new phrase.
     *
     * @param phraseDTO the phraseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new phraseDTO, or with status {@code 400 (Bad Request)} if the phrase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PhraseDTO> createPhrase(@Valid @RequestBody PhraseDTO phraseDTO) throws URISyntaxException, IOException {
        log.debug("REST request to save Phrase : {}", phraseDTO);
        if (phraseDTO.getId() != null) {
            throw new BadRequestAlertException("A new phrase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PhraseDTO result = phraseService.save(phraseDTO);
        return ResponseEntity.created(new URI("/api/phrases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /phrases/:id} : Updates an existing phrase.
     *
     * @param id the id of the phraseDTO to save.
     * @param phraseDTO the phraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phraseDTO,
     * or with status {@code 400 (Bad Request)} if the phraseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the phraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PhraseDTO> updatePhrase(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PhraseDTO phraseDTO
    ) throws URISyntaxException, IOException {
        log.debug("REST request to update Phrase : {}, {}", id, phraseDTO);
        if (phraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PhraseDTO result = phraseService.update(phraseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phraseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /phrases/:id} : Partial updates given fields of an existing phrase, field will ignore if it is null
     *
     * @param id the id of the phraseDTO to save.
     * @param phraseDTO the phraseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated phraseDTO,
     * or with status {@code 400 (Bad Request)} if the phraseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the phraseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the phraseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PhraseDTO> partialUpdatePhrase(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PhraseDTO phraseDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Phrase partially : {}, {}", id, phraseDTO);
        if (phraseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, phraseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!phraseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PhraseDTO> result = phraseService.partialUpdate(phraseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, phraseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /phrases} : get all the phrases.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of phrases in body.
     */
    @GetMapping("")
    public List<PhraseDTO> getAllPhrases(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Phrases");
        return phraseService.findAll();
    }

    @GetMapping("/author/{slug}")
    public ResponseEntity<AuthorPhrasesDTO> getAllPhrasesByAuthorSlug(
        @PathVariable("slug") String authorSlug,
        @RequestParam(value = "includeInactives", defaultValue = "false") boolean includeInactives
    ) {
        log.debug("REST request to get all Phrases by author slug: " + authorSlug);

        return phraseService
            .getAllByAuthorSlug(authorSlug, includeInactives)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code GET  /phrases/:id} : get the "id" phrase.
     *
     * @param id the id of the phraseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the phraseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PhraseDTO> getPhrase(@PathVariable("id") Long id) {
        log.debug("REST request to get Phrase : {}", id);
        Optional<PhraseDTO> phraseDTO = phraseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(phraseDTO);
    }

    /**
     * {@code DELETE  /phrases/:id} : delete the "id" phrase.
     *
     * @param id the id of the phraseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePhrase(@PathVariable("id") Long id) {
        log.debug("REST request to delete Phrase : {}", id);
        phraseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/featured")
    public List<PhraseDTO> getFeaturedTags() {
        log.debug("REST request to get all featured Phrases");
        return phraseService.findAllFeatured();
    }

    @PostMapping("{id}/sync/instagram")
    public ResponseEntity syncToInstagram(@PathVariable("id") Integer phraseId) throws IOException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:3000/").build();

        InstagramClient instagramClient = retrofit.create(InstagramClient.class);
        Call call = instagramClient.post(phraseId);

        Response response = call.execute();

        return ResponseEntity.status(response.code()).build();
    }
}
