package com.eulersantana.timelocation.web.rest;

import com.eulersantana.timelocation.TimeLocationApp;
import com.eulersantana.timelocation.domain.Local;
import com.eulersantana.timelocation.repository.LocalRepository;
import com.eulersantana.timelocation.service.LocalService;
import com.eulersantana.timelocation.repository.search.LocalSearchRepository;

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
 * Test class for the LocalResource REST controller.
 *
 * @see LocalResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeLocationApp.class)
@WebAppConfiguration
@IntegrationTest
public class LocalResourceIntTest {

    private static final String DEFAULT_PAIS = "AAAAA";
    private static final String UPDATED_PAIS = "BBBBB";
    private static final String DEFAULT_CIDADE = "AAAAA";
    private static final String UPDATED_CIDADE = "BBBBB";
    private static final String DEFAULT_ESTADO = "AAAAA";
    private static final String UPDATED_ESTADO = "BBBBB";

    @Inject
    private LocalRepository localRepository;

    @Inject
    private LocalService localService;

    @Inject
    private LocalSearchRepository localSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restLocalMockMvc;

    private Local local;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        LocalResource localResource = new LocalResource();
        ReflectionTestUtils.setField(localResource, "localService", localService);
        this.restLocalMockMvc = MockMvcBuilders.standaloneSetup(localResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        localSearchRepository.deleteAll();
        local = new Local();
        local.setPais(DEFAULT_PAIS);
        local.setCidade(DEFAULT_CIDADE);
        local.setEstado(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    public void createLocal() throws Exception {
        int databaseSizeBeforeCreate = localRepository.findAll().size();

        // Create the Local

        restLocalMockMvc.perform(post("/api/locals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(local)))
                .andExpect(status().isCreated());

        // Validate the Local in the database
        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeCreate + 1);
        Local testLocal = locals.get(locals.size() - 1);
        assertThat(testLocal.getPais()).isEqualTo(DEFAULT_PAIS);
        assertThat(testLocal.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testLocal.getEstado()).isEqualTo(DEFAULT_ESTADO);

        // Validate the Local in ElasticSearch
        Local localEs = localSearchRepository.findOne(testLocal.getId());
        assertThat(localEs).isEqualToComparingFieldByField(testLocal);
    }

    @Test
    @Transactional
    public void checkPaisIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setPais(null);

        // Create the Local, which fails.

        restLocalMockMvc.perform(post("/api/locals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(local)))
                .andExpect(status().isBadRequest());

        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCidadeIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setCidade(null);

        // Create the Local, which fails.

        restLocalMockMvc.perform(post("/api/locals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(local)))
                .andExpect(status().isBadRequest());

        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEstadoIsRequired() throws Exception {
        int databaseSizeBeforeTest = localRepository.findAll().size();
        // set the field null
        local.setEstado(null);

        // Create the Local, which fails.

        restLocalMockMvc.perform(post("/api/locals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(local)))
                .andExpect(status().isBadRequest());

        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLocals() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get all the locals
        restLocalMockMvc.perform(get("/api/locals?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
                .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS.toString())))
                .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
                .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    public void getLocal() throws Exception {
        // Initialize the database
        localRepository.saveAndFlush(local);

        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(local.getId().intValue()))
            .andExpect(jsonPath("$.pais").value(DEFAULT_PAIS.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingLocal() throws Exception {
        // Get the local
        restLocalMockMvc.perform(get("/api/locals/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLocal() throws Exception {
        // Initialize the database
        localService.save(local);

        int databaseSizeBeforeUpdate = localRepository.findAll().size();

        // Update the local
        Local updatedLocal = new Local();
        updatedLocal.setId(local.getId());
        updatedLocal.setPais(UPDATED_PAIS);
        updatedLocal.setCidade(UPDATED_CIDADE);
        updatedLocal.setEstado(UPDATED_ESTADO);

        restLocalMockMvc.perform(put("/api/locals")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedLocal)))
                .andExpect(status().isOk());

        // Validate the Local in the database
        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeUpdate);
        Local testLocal = locals.get(locals.size() - 1);
        assertThat(testLocal.getPais()).isEqualTo(UPDATED_PAIS);
        assertThat(testLocal.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testLocal.getEstado()).isEqualTo(UPDATED_ESTADO);

        // Validate the Local in ElasticSearch
        Local localEs = localSearchRepository.findOne(testLocal.getId());
        assertThat(localEs).isEqualToComparingFieldByField(testLocal);
    }

    @Test
    @Transactional
    public void deleteLocal() throws Exception {
        // Initialize the database
        localService.save(local);

        int databaseSizeBeforeDelete = localRepository.findAll().size();

        // Get the local
        restLocalMockMvc.perform(delete("/api/locals/{id}", local.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean localExistsInEs = localSearchRepository.exists(local.getId());
        assertThat(localExistsInEs).isFalse();

        // Validate the database is empty
        List<Local> locals = localRepository.findAll();
        assertThat(locals).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchLocal() throws Exception {
        // Initialize the database
        localService.save(local);

        // Search the local
        restLocalMockMvc.perform(get("/api/_search/locals?query=id:" + local.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(local.getId().intValue())))
            .andExpect(jsonPath("$.[*].pais").value(hasItem(DEFAULT_PAIS.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }
}
