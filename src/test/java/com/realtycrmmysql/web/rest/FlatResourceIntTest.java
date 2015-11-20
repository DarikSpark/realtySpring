package com.realtycrmmysql.web.rest;

import com.realtycrmmysql.Application;
import com.realtycrmmysql.domain.Flat;
import com.realtycrmmysql.repository.FlatRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the FlatResource REST controller.
 *
 * @see FlatResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FlatResourceIntTest {

    private static final String DEFAULT_ADDRESS = "A";
    private static final String UPDATED_ADDRESS = "B";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    private static final Integer DEFAULT_DAY_COST = 1;
    private static final Integer UPDATED_DAY_COST = 2;

    private static final Integer DEFAULT_PEOPLE_COUNT = 1;
    private static final Integer UPDATED_PEOPLE_COUNT = 2;

    @Inject
    private FlatRepository flatRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFlatMockMvc;

    private Flat flat;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FlatResource flatResource = new FlatResource();
        ReflectionTestUtils.setField(flatResource, "flatRepository", flatRepository);
        this.restFlatMockMvc = MockMvcBuilders.standaloneSetup(flatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        flat = new Flat();
        flat.setAddress(DEFAULT_ADDRESS);
        flat.setDescription(DEFAULT_DESCRIPTION);
        flat.setDay_cost(DEFAULT_DAY_COST);
        flat.setPeople_count(DEFAULT_PEOPLE_COUNT);
    }

    @Test
    @Transactional
    public void createFlat() throws Exception {
        int databaseSizeBeforeCreate = flatRepository.findAll().size();

        // Create the Flat

        restFlatMockMvc.perform(post("/api/flats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flat)))
                .andExpect(status().isCreated());

        // Validate the Flat in the database
        List<Flat> flats = flatRepository.findAll();
        assertThat(flats).hasSize(databaseSizeBeforeCreate + 1);
        Flat testFlat = flats.get(flats.size() - 1);
        assertThat(testFlat.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testFlat.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testFlat.getDay_cost()).isEqualTo(DEFAULT_DAY_COST);
        assertThat(testFlat.getPeople_count()).isEqualTo(DEFAULT_PEOPLE_COUNT);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = flatRepository.findAll().size();
        // set the field null
        flat.setAddress(null);

        // Create the Flat, which fails.

        restFlatMockMvc.perform(post("/api/flats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flat)))
                .andExpect(status().isBadRequest());

        List<Flat> flats = flatRepository.findAll();
        assertThat(flats).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFlats() throws Exception {
        // Initialize the database
        flatRepository.saveAndFlush(flat);

        // Get all the flats
        restFlatMockMvc.perform(get("/api/flats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(flat.getId().intValue())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].day_cost").value(hasItem(DEFAULT_DAY_COST)))
                .andExpect(jsonPath("$.[*].people_count").value(hasItem(DEFAULT_PEOPLE_COUNT)));
    }

    @Test
    @Transactional
    public void getFlat() throws Exception {
        // Initialize the database
        flatRepository.saveAndFlush(flat);

        // Get the flat
        restFlatMockMvc.perform(get("/api/flats/{id}", flat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(flat.getId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.day_cost").value(DEFAULT_DAY_COST))
            .andExpect(jsonPath("$.people_count").value(DEFAULT_PEOPLE_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingFlat() throws Exception {
        // Get the flat
        restFlatMockMvc.perform(get("/api/flats/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFlat() throws Exception {
        // Initialize the database
        flatRepository.saveAndFlush(flat);

		int databaseSizeBeforeUpdate = flatRepository.findAll().size();

        // Update the flat
        flat.setAddress(UPDATED_ADDRESS);
        flat.setDescription(UPDATED_DESCRIPTION);
        flat.setDay_cost(UPDATED_DAY_COST);
        flat.setPeople_count(UPDATED_PEOPLE_COUNT);

        restFlatMockMvc.perform(put("/api/flats")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(flat)))
                .andExpect(status().isOk());

        // Validate the Flat in the database
        List<Flat> flats = flatRepository.findAll();
        assertThat(flats).hasSize(databaseSizeBeforeUpdate);
        Flat testFlat = flats.get(flats.size() - 1);
        assertThat(testFlat.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testFlat.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testFlat.getDay_cost()).isEqualTo(UPDATED_DAY_COST);
        assertThat(testFlat.getPeople_count()).isEqualTo(UPDATED_PEOPLE_COUNT);
    }

    @Test
    @Transactional
    public void deleteFlat() throws Exception {
        // Initialize the database
        flatRepository.saveAndFlush(flat);

		int databaseSizeBeforeDelete = flatRepository.findAll().size();

        // Get the flat
        restFlatMockMvc.perform(delete("/api/flats/{id}", flat.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Flat> flats = flatRepository.findAll();
        assertThat(flats).hasSize(databaseSizeBeforeDelete - 1);
    }
}
