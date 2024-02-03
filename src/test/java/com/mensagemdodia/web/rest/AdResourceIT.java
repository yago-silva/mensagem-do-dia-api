package com.mensagemdodia.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mensagemdodia.IntegrationTest;
import com.mensagemdodia.domain.Ad;
import com.mensagemdodia.repository.AdRepository;
import com.mensagemdodia.service.AdService;
import com.mensagemdodia.service.dto.AdDTO;
import com.mensagemdodia.service.mapper.AdMapper;
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
 * Integration tests for the {@link AdResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdResourceIT {

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOCALE = "AAAAAAAAAA";
    private static final String UPDATED_LOCALE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_DEVICE_TYPE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_FEATURED = false;
    private static final Boolean UPDATED_FEATURED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_AFFILIATE_LINK = "AAAAAAAAAA";
    private static final String UPDATED_AFFILIATE_LINK = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ads";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdRepository adRepository;

    @Mock
    private AdRepository adRepositoryMock;

    @Autowired
    private AdMapper adMapper;

    @Mock
    private AdService adServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdMockMvc;

    private Ad ad;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ad createEntity(EntityManager em) {
        Ad ad = new Ad()
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .locale(DEFAULT_LOCALE)
            .deviceType(DEFAULT_DEVICE_TYPE)
            .featured(DEFAULT_FEATURED)
            .active(DEFAULT_ACTIVE)
            .affiliateLink(DEFAULT_AFFILIATE_LINK);
        return ad;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ad createUpdatedEntity(EntityManager em) {
        Ad ad = new Ad()
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .locale(UPDATED_LOCALE)
            .deviceType(UPDATED_DEVICE_TYPE)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .affiliateLink(UPDATED_AFFILIATE_LINK);
        return ad;
    }

    @BeforeEach
    public void initTest() {
        ad = createEntity(em);
    }

    @Test
    @Transactional
    void createAd() throws Exception {
        int databaseSizeBeforeCreate = adRepository.findAll().size();
        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);
        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isCreated());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeCreate + 1);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testAd.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testAd.getLocale()).isEqualTo(DEFAULT_LOCALE);
        assertThat(testAd.getDeviceType()).isEqualTo(DEFAULT_DEVICE_TYPE);
        assertThat(testAd.getFeatured()).isEqualTo(DEFAULT_FEATURED);
        assertThat(testAd.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAd.getAffiliateLink()).isEqualTo(DEFAULT_AFFILIATE_LINK);
    }

    @Test
    @Transactional
    void createAdWithExistingId() throws Exception {
        // Create the Ad with an existing ID
        ad.setId(1L);
        AdDTO adDTO = adMapper.toDto(ad);

        int databaseSizeBeforeCreate = adRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setCreatedAt(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setUpdatedAt(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocaleIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setLocale(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeviceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setDeviceType(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFeaturedIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setFeatured(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = adRepository.findAll().size();
        // set the field null
        ad.setActive(null);

        // Create the Ad, which fails.
        AdDTO adDTO = adMapper.toDto(ad);

        restAdMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isBadRequest());

        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAds() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        // Get all the adList
        restAdMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ad.getId().intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].locale").value(hasItem(DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].deviceType").value(hasItem(DEFAULT_DEVICE_TYPE)))
            .andExpect(jsonPath("$.[*].featured").value(hasItem(DEFAULT_FEATURED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].affiliateLink").value(hasItem(DEFAULT_AFFILIATE_LINK)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdsWithEagerRelationshipsIsEnabled() throws Exception {
        when(adServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(adServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(adServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(adRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAd() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        // Get the ad
        restAdMockMvc
            .perform(get(ENTITY_API_URL_ID, ad.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ad.getId().intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.locale").value(DEFAULT_LOCALE))
            .andExpect(jsonPath("$.deviceType").value(DEFAULT_DEVICE_TYPE))
            .andExpect(jsonPath("$.featured").value(DEFAULT_FEATURED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.affiliateLink").value(DEFAULT_AFFILIATE_LINK));
    }

    @Test
    @Transactional
    void getNonExistingAd() throws Exception {
        // Get the ad
        restAdMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAd() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        int databaseSizeBeforeUpdate = adRepository.findAll().size();

        // Update the ad
        Ad updatedAd = adRepository.findById(ad.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAd are not directly saved in db
        em.detach(updatedAd);
        updatedAd
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .locale(UPDATED_LOCALE)
            .deviceType(UPDATED_DEVICE_TYPE)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .affiliateLink(UPDATED_AFFILIATE_LINK);
        AdDTO adDTO = adMapper.toDto(updatedAd);

        restAdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adDTO))
            )
            .andExpect(status().isOk());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAd.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAd.getLocale()).isEqualTo(UPDATED_LOCALE);
        assertThat(testAd.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testAd.getFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testAd.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAd.getAffiliateLink()).isEqualTo(UPDATED_AFFILIATE_LINK);
    }

    @Test
    @Transactional
    void putNonExistingAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, adDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(adDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdWithPatch() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        int databaseSizeBeforeUpdate = adRepository.findAll().size();

        // Update the ad using partial update
        Ad partialUpdatedAd = new Ad();
        partialUpdatedAd.setId(ad.getId());

        partialUpdatedAd
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .deviceType(UPDATED_DEVICE_TYPE)
            .featured(UPDATED_FEATURED);

        restAdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAd))
            )
            .andExpect(status().isOk());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAd.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAd.getLocale()).isEqualTo(DEFAULT_LOCALE);
        assertThat(testAd.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testAd.getFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testAd.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testAd.getAffiliateLink()).isEqualTo(DEFAULT_AFFILIATE_LINK);
    }

    @Test
    @Transactional
    void fullUpdateAdWithPatch() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        int databaseSizeBeforeUpdate = adRepository.findAll().size();

        // Update the ad using partial update
        Ad partialUpdatedAd = new Ad();
        partialUpdatedAd.setId(ad.getId());

        partialUpdatedAd
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .locale(UPDATED_LOCALE)
            .deviceType(UPDATED_DEVICE_TYPE)
            .featured(UPDATED_FEATURED)
            .active(UPDATED_ACTIVE)
            .affiliateLink(UPDATED_AFFILIATE_LINK);

        restAdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAd.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAd))
            )
            .andExpect(status().isOk());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
        Ad testAd = adList.get(adList.size() - 1);
        assertThat(testAd.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testAd.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testAd.getLocale()).isEqualTo(UPDATED_LOCALE);
        assertThat(testAd.getDeviceType()).isEqualTo(UPDATED_DEVICE_TYPE);
        assertThat(testAd.getFeatured()).isEqualTo(UPDATED_FEATURED);
        assertThat(testAd.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testAd.getAffiliateLink()).isEqualTo(UPDATED_AFFILIATE_LINK);
    }

    @Test
    @Transactional
    void patchNonExistingAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, adDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(adDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAd() throws Exception {
        int databaseSizeBeforeUpdate = adRepository.findAll().size();
        ad.setId(longCount.incrementAndGet());

        // Create the Ad
        AdDTO adDTO = adMapper.toDto(ad);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(adDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ad in the database
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAd() throws Exception {
        // Initialize the database
        adRepository.saveAndFlush(ad);

        int databaseSizeBeforeDelete = adRepository.findAll().size();

        // Delete the ad
        restAdMockMvc.perform(delete(ENTITY_API_URL_ID, ad.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ad> adList = adRepository.findAll();
        assertThat(adList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
