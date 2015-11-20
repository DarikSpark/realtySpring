package com.realtycrmmysql.web.rest;

import com.realtycrmmysql.Application;
import com.realtycrmmysql.domain.Owner;
import com.realtycrmmysql.repository.OwnerRepository;

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
 * Test class for the OwnerResource REST controller.
 *
 * @see OwnerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class OwnerResourceIntTest {

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";
    private static final String DEFAULT_PHONE = "AAAAA";
    private static final String UPDATED_PHONE = "BBBBB";

    private static final Integer DEFAULT_BANK_ACCOUNT = 1;
    private static final Integer UPDATED_BANK_ACCOUNT = 2;

    @Inject
    private OwnerRepository ownerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restOwnerMockMvc;

    private Owner owner;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        OwnerResource ownerResource = new OwnerResource();
        ReflectionTestUtils.setField(ownerResource, "ownerRepository", ownerRepository);
        this.restOwnerMockMvc = MockMvcBuilders.standaloneSetup(ownerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        owner = new Owner();
        owner.setName(DEFAULT_NAME);
        owner.setPhone(DEFAULT_PHONE);
        owner.setBank_account(DEFAULT_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void createOwner() throws Exception {
        int databaseSizeBeforeCreate = ownerRepository.findAll().size();

        // Create the Owner

        restOwnerMockMvc.perform(post("/api/owners")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(owner)))
                .andExpect(status().isCreated());

        // Validate the Owner in the database
        List<Owner> owners = ownerRepository.findAll();
        assertThat(owners).hasSize(databaseSizeBeforeCreate + 1);
        Owner testOwner = owners.get(owners.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOwner.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testOwner.getBank_account()).isEqualTo(DEFAULT_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ownerRepository.findAll().size();
        // set the field null
        owner.setName(null);

        // Create the Owner, which fails.

        restOwnerMockMvc.perform(post("/api/owners")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(owner)))
                .andExpect(status().isBadRequest());

        List<Owner> owners = ownerRepository.findAll();
        assertThat(owners).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOwners() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get all the owners
        restOwnerMockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(owner.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.toString())))
                .andExpect(jsonPath("$.[*].bank_account").value(hasItem(DEFAULT_BANK_ACCOUNT)));
    }

    @Test
    @Transactional
    public void getOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", owner.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(owner.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.toString()))
            .andExpect(jsonPath("$.bank_account").value(DEFAULT_BANK_ACCOUNT));
    }

    @Test
    @Transactional
    public void getNonExistingOwner() throws Exception {
        // Get the owner
        restOwnerMockMvc.perform(get("/api/owners/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

		int databaseSizeBeforeUpdate = ownerRepository.findAll().size();

        // Update the owner
        owner.setName(UPDATED_NAME);
        owner.setPhone(UPDATED_PHONE);
        owner.setBank_account(UPDATED_BANK_ACCOUNT);

        restOwnerMockMvc.perform(put("/api/owners")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(owner)))
                .andExpect(status().isOk());

        // Validate the Owner in the database
        List<Owner> owners = ownerRepository.findAll();
        assertThat(owners).hasSize(databaseSizeBeforeUpdate);
        Owner testOwner = owners.get(owners.size() - 1);
        assertThat(testOwner.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOwner.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testOwner.getBank_account()).isEqualTo(UPDATED_BANK_ACCOUNT);
    }

    @Test
    @Transactional
    public void deleteOwner() throws Exception {
        // Initialize the database
        ownerRepository.saveAndFlush(owner);

		int databaseSizeBeforeDelete = ownerRepository.findAll().size();

        // Get the owner
        restOwnerMockMvc.perform(delete("/api/owners/{id}", owner.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Owner> owners = ownerRepository.findAll();
        assertThat(owners).hasSize(databaseSizeBeforeDelete - 1);
    }
}
