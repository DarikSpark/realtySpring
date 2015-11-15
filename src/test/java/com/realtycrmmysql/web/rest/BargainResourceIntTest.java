package com.realtycrmmysql.web.rest;

import com.realtycrmmysql.Application;
import com.realtycrmmysql.domain.Bargain;
import com.realtycrmmysql.repository.BargainRepository;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the BargainResource REST controller.
 *
 * @see BargainResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BargainResourceIntTest {


    private static final Integer DEFAULT_DAY_COUNT = 0;
    private static final Integer UPDATED_DAY_COUNT = 1;

    private static final LocalDate DEFAULT_COMING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_COMING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_NOTE = "AAAAA";
    private static final String UPDATED_NOTE = "BBBBB";

    @Inject
    private BargainRepository bargainRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBargainMockMvc;

    private Bargain bargain;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BargainResource bargainResource = new BargainResource();
        ReflectionTestUtils.setField(bargainResource, "bargainRepository", bargainRepository);
        this.restBargainMockMvc = MockMvcBuilders.standaloneSetup(bargainResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        bargain = new Bargain();
        bargain.setDay_count(DEFAULT_DAY_COUNT);
        bargain.setComing_date(DEFAULT_COMING_DATE);
        bargain.setNote(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void createBargain() throws Exception {
        int databaseSizeBeforeCreate = bargainRepository.findAll().size();

        // Create the Bargain

        restBargainMockMvc.perform(post("/api/bargains")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bargain)))
                .andExpect(status().isCreated());

        // Validate the Bargain in the database
        List<Bargain> bargains = bargainRepository.findAll();
        assertThat(bargains).hasSize(databaseSizeBeforeCreate + 1);
        Bargain testBargain = bargains.get(bargains.size() - 1);
        assertThat(testBargain.getDay_count()).isEqualTo(DEFAULT_DAY_COUNT);
        assertThat(testBargain.getComing_date()).isEqualTo(DEFAULT_COMING_DATE);
        assertThat(testBargain.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    public void checkComing_dateIsRequired() throws Exception {
        int databaseSizeBeforeTest = bargainRepository.findAll().size();
        // set the field null
        bargain.setComing_date(null);

        // Create the Bargain, which fails.

        restBargainMockMvc.perform(post("/api/bargains")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bargain)))
                .andExpect(status().isBadRequest());

        List<Bargain> bargains = bargainRepository.findAll();
        assertThat(bargains).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBargains() throws Exception {
        // Initialize the database
        bargainRepository.saveAndFlush(bargain);

        // Get all the bargains
        restBargainMockMvc.perform(get("/api/bargains"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(bargain.getId().intValue())))
                .andExpect(jsonPath("$.[*].day_count").value(hasItem(DEFAULT_DAY_COUNT)))
                .andExpect(jsonPath("$.[*].coming_date").value(hasItem(DEFAULT_COMING_DATE.toString())))
                .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE.toString())));
    }

    @Test
    @Transactional
    public void getBargain() throws Exception {
        // Initialize the database
        bargainRepository.saveAndFlush(bargain);

        // Get the bargain
        restBargainMockMvc.perform(get("/api/bargains/{id}", bargain.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(bargain.getId().intValue()))
            .andExpect(jsonPath("$.day_count").value(DEFAULT_DAY_COUNT))
            .andExpect(jsonPath("$.coming_date").value(DEFAULT_COMING_DATE.toString()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBargain() throws Exception {
        // Get the bargain
        restBargainMockMvc.perform(get("/api/bargains/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBargain() throws Exception {
        // Initialize the database
        bargainRepository.saveAndFlush(bargain);

		int databaseSizeBeforeUpdate = bargainRepository.findAll().size();

        // Update the bargain
        bargain.setDay_count(UPDATED_DAY_COUNT);
        bargain.setComing_date(UPDATED_COMING_DATE);
        bargain.setNote(UPDATED_NOTE);

        restBargainMockMvc.perform(put("/api/bargains")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(bargain)))
                .andExpect(status().isOk());

        // Validate the Bargain in the database
        List<Bargain> bargains = bargainRepository.findAll();
        assertThat(bargains).hasSize(databaseSizeBeforeUpdate);
        Bargain testBargain = bargains.get(bargains.size() - 1);
        assertThat(testBargain.getDay_count()).isEqualTo(UPDATED_DAY_COUNT);
        assertThat(testBargain.getComing_date()).isEqualTo(UPDATED_COMING_DATE);
        assertThat(testBargain.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void deleteBargain() throws Exception {
        // Initialize the database
        bargainRepository.saveAndFlush(bargain);

		int databaseSizeBeforeDelete = bargainRepository.findAll().size();

        // Get the bargain
        restBargainMockMvc.perform(delete("/api/bargains/{id}", bargain.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Bargain> bargains = bargainRepository.findAll();
        assertThat(bargains).hasSize(databaseSizeBeforeDelete - 1);
    }
}
