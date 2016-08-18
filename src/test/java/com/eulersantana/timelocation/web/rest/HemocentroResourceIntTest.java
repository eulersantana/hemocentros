package com.eulersantana.timelocation.web.rest;

import com.eulersantana.timelocation.TimeLocationApp;
import com.eulersantana.timelocation.domain.Hemocentro;
import com.eulersantana.timelocation.repository.HemocentroRepository;
import com.eulersantana.timelocation.service.HemocentroService;
import com.eulersantana.timelocation.repository.search.HemocentroSearchRepository;

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
 * Test class for the HemocentroResource REST controller.
 *
 * @see HemocentroResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeLocationApp.class)
@WebAppConfiguration
@IntegrationTest
public class HemocentroResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";
    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final Boolean DEFAULT_MOVEL = false;
    private static final Boolean UPDATED_MOVEL = true;

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private HemocentroRepository hemocentroRepository;

    @Inject
    private HemocentroService hemocentroService;

    @Inject
    private HemocentroSearchRepository hemocentroSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restHemocentroMockMvc;

    private Hemocentro hemocentro;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        HemocentroResource hemocentroResource = new HemocentroResource();
        ReflectionTestUtils.setField(hemocentroResource, "hemocentroService", hemocentroService);
        this.restHemocentroMockMvc = MockMvcBuilders.standaloneSetup(hemocentroResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        hemocentroSearchRepository.deleteAll();
        hemocentro = new Hemocentro();
        hemocentro.setNome(DEFAULT_NOME);
        hemocentro.setEmail(DEFAULT_EMAIL);
        hemocentro.setMovel(DEFAULT_MOVEL);
        hemocentro.setStatus(DEFAULT_STATUS);
        hemocentro.setCreatedAt(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createHemocentro() throws Exception {
        int databaseSizeBeforeCreate = hemocentroRepository.findAll().size();

        // Create the Hemocentro

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isCreated());

        // Validate the Hemocentro in the database
        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeCreate + 1);
        Hemocentro testHemocentro = hemocentros.get(hemocentros.size() - 1);
        assertThat(testHemocentro.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testHemocentro.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testHemocentro.isMovel()).isEqualTo(DEFAULT_MOVEL);
        assertThat(testHemocentro.isStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testHemocentro.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);

        // Validate the Hemocentro in ElasticSearch
        Hemocentro hemocentroEs = hemocentroSearchRepository.findOne(testHemocentro.getId());
        assertThat(hemocentroEs).isEqualToComparingFieldByField(testHemocentro);
    }

    @Test
    @Transactional
    public void checkNomeIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemocentroRepository.findAll().size();
        // set the field null
        hemocentro.setNome(null);

        // Create the Hemocentro, which fails.

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isBadRequest());

        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemocentroRepository.findAll().size();
        // set the field null
        hemocentro.setEmail(null);

        // Create the Hemocentro, which fails.

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isBadRequest());

        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkMovelIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemocentroRepository.findAll().size();
        // set the field null
        hemocentro.setMovel(null);

        // Create the Hemocentro, which fails.

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isBadRequest());

        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemocentroRepository.findAll().size();
        // set the field null
        hemocentro.setStatus(null);

        // Create the Hemocentro, which fails.

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isBadRequest());

        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = hemocentroRepository.findAll().size();
        // set the field null
        hemocentro.setCreatedAt(null);

        // Create the Hemocentro, which fails.

        restHemocentroMockMvc.perform(post("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(hemocentro)))
                .andExpect(status().isBadRequest());

        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHemocentros() throws Exception {
        // Initialize the database
        hemocentroRepository.saveAndFlush(hemocentro);

        // Get all the hemocentros
        restHemocentroMockMvc.perform(get("/api/hemocentros?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(hemocentro.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].movel").value(hasItem(DEFAULT_MOVEL.booleanValue())))
                .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getHemocentro() throws Exception {
        // Initialize the database
        hemocentroRepository.saveAndFlush(hemocentro);

        // Get the hemocentro
        restHemocentroMockMvc.perform(get("/api/hemocentros/{id}", hemocentro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(hemocentro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.movel").value(DEFAULT_MOVEL.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHemocentro() throws Exception {
        // Get the hemocentro
        restHemocentroMockMvc.perform(get("/api/hemocentros/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHemocentro() throws Exception {
        // Initialize the database
        hemocentroService.save(hemocentro);

        int databaseSizeBeforeUpdate = hemocentroRepository.findAll().size();

        // Update the hemocentro
        Hemocentro updatedHemocentro = new Hemocentro();
        updatedHemocentro.setId(hemocentro.getId());
        updatedHemocentro.setNome(UPDATED_NOME);
        updatedHemocentro.setEmail(UPDATED_EMAIL);
        updatedHemocentro.setMovel(UPDATED_MOVEL);
        updatedHemocentro.setStatus(UPDATED_STATUS);
        updatedHemocentro.setCreatedAt(UPDATED_CREATED_AT);

        restHemocentroMockMvc.perform(put("/api/hemocentros")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedHemocentro)))
                .andExpect(status().isOk());

        // Validate the Hemocentro in the database
        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeUpdate);
        Hemocentro testHemocentro = hemocentros.get(hemocentros.size() - 1);
        assertThat(testHemocentro.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testHemocentro.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testHemocentro.isMovel()).isEqualTo(UPDATED_MOVEL);
        assertThat(testHemocentro.isStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testHemocentro.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);

        // Validate the Hemocentro in ElasticSearch
        Hemocentro hemocentroEs = hemocentroSearchRepository.findOne(testHemocentro.getId());
        assertThat(hemocentroEs).isEqualToComparingFieldByField(testHemocentro);
    }

    @Test
    @Transactional
    public void deleteHemocentro() throws Exception {
        // Initialize the database
        hemocentroService.save(hemocentro);

        int databaseSizeBeforeDelete = hemocentroRepository.findAll().size();

        // Get the hemocentro
        restHemocentroMockMvc.perform(delete("/api/hemocentros/{id}", hemocentro.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean hemocentroExistsInEs = hemocentroSearchRepository.exists(hemocentro.getId());
        assertThat(hemocentroExistsInEs).isFalse();

        // Validate the database is empty
        List<Hemocentro> hemocentros = hemocentroRepository.findAll();
        assertThat(hemocentros).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchHemocentro() throws Exception {
        // Initialize the database
        hemocentroService.save(hemocentro);

        // Search the hemocentro
        restHemocentroMockMvc.perform(get("/api/_search/hemocentros?query=id:" + hemocentro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hemocentro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].movel").value(hasItem(DEFAULT_MOVEL.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())));
    }
}
