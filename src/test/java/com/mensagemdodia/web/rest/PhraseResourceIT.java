package com.mensagemdodia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mensagemdodia.IntegrationTest;
import com.mensagemdodia.domain.Phrase;
import com.mensagemdodia.repository.PhraseRepository;
import com.mensagemdodia.service.PhraseService;
import com.mensagemdodia.service.dto.PhraseDTO;
import com.mensagemdodia.service.mapper.PhraseMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PhraseResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PhraseResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_FEATURED = false;
    private static final Boolean UPDATED_FEATURED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_SLUG = "AAAAAAAAAA";
    private static final String UPDATED_SLUG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/phrases";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PhraseRepository phraseRepository;

    @Mock
    private PhraseRepository phraseRepositoryMock;

    @Autowired
    private PhraseMapper phraseMapper;

    @Mock
    private PhraseService phraseServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPhraseMockMvc;

    private Phrase phrase;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phrase createEntity(EntityManager em) {
        Phrase phrase = new Phrase()
            .content(DEFAULT_CONTENT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .featured(DEFAULT_FEATURED)
            .active(DEFAULT_ACTIVE)
            .slug(DEFAULT_SLUG);
        return phrase;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Phrase createUpdatedEntity(EntityManager em) {
        Phrase phrase = new Phrase()
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .slug(UPDATED_SLUG);
        return phrase;
    }

    @BeforeEach
    public void initTest() {
        phrase = createEntity(em);
    }

    @Test
    @Transactional
    void createPhrase() throws Exception {
        int databaseSizeBeforeCreate = phraseRepository.findAll().size();
        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);
        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isCreated());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeCreate + 1);
        Phrase testPhrase = phraseList.get(phraseList.size() - 1);
        assertThat(testPhrase.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPhrase.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testPhrase.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPhrase.getFeatured()).isEqualTo(DEFAULT_FEATURED);
        assertThat(testPhrase.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPhrase.getSlug()).isEqualTo(DEFAULT_SLUG);
    }

    @Test
    @Transactional
    void createPhraseWithExistingId() throws Exception {
        // Create the Phrase with an existing ID
        phrase.setId(1L);
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        int databaseSizeBeforeCreate = phraseRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setContent(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setCreatedAt(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setUpdatedAt(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeaturedIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setFeatured(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setActive(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSlugIsRequired() throws Exception {
        int databaseSizeBeforeTest = phraseRepository.findAll().size();
        // set the field null
        phrase.setSlug(null);

        // Create the Phrase, which fails.
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        restPhraseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isBadRequest());

        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPhrases() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        // Get all the phraseList
        restPhraseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(phrase.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].slug").value(hasItem(DEFAULT_SLUG)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPhrasesWithEagerRelationshipsIsEnabled() throws Exception {
        when(phraseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPhraseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(phraseServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPhrasesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(phraseServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPhraseMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(phraseRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPhrase() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        // Get the phrase
        restPhraseMockMvc
            .perform(get(ENTITY_API_URL_ID, phrase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(phrase.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.featured").value(DEFAULT_FEATURED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.slug").value(DEFAULT_SLUG));
    }

    @Test
    @Transactional
    void getNonExistingPhrase() throws Exception {
        // Get the phrase
        restPhraseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPhrase() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();

        // Update the phrase
        Phrase updatedPhrase = phraseRepository.findById(phrase.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPhrase are not directly saved in db
        em.detach(updatedPhrase);
        updatedPhrase
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .slug(UPDATED_SLUG);
        PhraseDTO phraseDTO = phraseMapper.toDto(updatedPhrase);

        restPhraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isOk());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
        Phrase testPhrase = phraseList.get(phraseList.size() - 1);
        assertThat(testPhrase.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPhrase.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPhrase.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPhrase.getFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testPhrase.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPhrase.getSlug()).isEqualTo(UPDATED_SLUG);
    }

    @Test
    @Transactional
    void putNonExistingPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, phraseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(phraseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePhraseWithPatch() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();

        // Update the phrase using partial update
        Phrase partialUpdatedPhrase = new Phrase();
        partialUpdatedPhrase.setId(phrase.getId());

        partialUpdatedPhrase.createdAt(UPDATED_CREATED_AT).active(UPDATED_ACTIVE).slug(UPDATED_SLUG);

        restPhraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhrase))
            )
            .andExpect(status().isOk());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
        Phrase testPhrase = phraseList.get(phraseList.size() - 1);
        assertThat(testPhrase.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testPhrase.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPhrase.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testPhrase.getFeatured()).isEqualTo(DEFAULT_FEATURED);
        assertThat(testPhrase.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPhrase.getSlug()).isEqualTo(UPDATED_SLUG);
    }

    @Test
    @Transactional
    void fullUpdatePhraseWithPatch() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();

        // Update the phrase using partial update
        Phrase partialUpdatedPhrase = new Phrase();
        partialUpdatedPhrase.setId(phrase.getId());

        partialUpdatedPhrase
            .content(UPDATED_CONTENT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .slug(UPDATED_SLUG);

        restPhraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPhrase.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPhrase))
            )
            .andExpect(status().isOk());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
        Phrase testPhrase = phraseList.get(phraseList.size() - 1);
        assertThat(testPhrase.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testPhrase.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testPhrase.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testPhrase.getFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testPhrase.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPhrase.getSlug()).isEqualTo(UPDATED_SLUG);
    }

    @Test
    @Transactional
    void patchNonExistingPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, phraseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPhrase() throws Exception {
        int databaseSizeBeforeUpdate = phraseRepository.findAll().size();
        phrase.setId(longCount.incrementAndGet());

        // Create the Phrase
        PhraseDTO phraseDTO = phraseMapper.toDto(phrase);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPhraseMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(phraseDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Phrase in the database
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePhrase() throws Exception {
        // Initialize the database
        phraseRepository.saveAndFlush(phrase);

        int databaseSizeBeforeDelete = phraseRepository.findAll().size();

        // Delete the phrase
        restPhraseMockMvc
            .perform(delete(ENTITY_API_URL_ID, phrase.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Phrase> phraseList = phraseRepository.findAll();
        assertThat(phraseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
