package com.eulersantana.timelocation.web.rest;

import com.eulersantana.timelocation.TimeLocationApp;
import com.eulersantana.timelocation.domain.Telefone;
import com.eulersantana.timelocation.repository.TelefoneRepository;
import com.eulersantana.timelocation.service.TelefoneService;
import com.eulersantana.timelocation.repository.search.TelefoneSearchRepository;

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
 * Test class for the TelefoneResource REST controller.
 *
 * @see TelefoneResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeLocationApp.class)
@WebAppConfiguration
@IntegrationTest
public class TelefoneResourceIntTest {

    private static final String DEFAULT_NUMERO = "AAAAA";
    private static final String UPDATED_NUMERO = "BBBBB";

    private static final Integer DEFAULT_DDD = 1;
    private static final Integer UPDATED_DDD = 2;

    private static final Integer DEFAULT_DDI = 1;
    private static final Integer UPDATED_DDI = 2;

    private static final Boolean DEFAULT_FIXO = false;
    private static final Boolean UPDATED_FIXO = true;

    @Inject
    private TelefoneRepository telefoneRepository;

    @Inject
    private TelefoneService telefoneService;

    @Inject
    private TelefoneSearchRepository telefoneSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTelefoneMockMvc;

    private Telefone telefone;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TelefoneResource telefoneResource = new TelefoneResource();
        ReflectionTestUtils.setField(telefoneResource, "telefoneService", telefoneService);
        this.restTelefoneMockMvc = MockMvcBuilders.standaloneSetup(telefoneResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        telefoneSearchRepository.deleteAll();
        telefone = new Telefone();
        telefone.setNumero(DEFAULT_NUMERO);
        telefone.setDdd(DEFAULT_DDD);
        telefone.setDdi(DEFAULT_DDI);
        telefone.setFixo(DEFAULT_FIXO);
    }

    @Test
    @Transactional
    public void createTelefone() throws Exception {
        int databaseSizeBeforeCreate = telefoneRepository.findAll().size();

        // Create the Telefone

        restTelefoneMockMvc.perform(post("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefone)))
                .andExpect(status().isCreated());

        // Validate the Telefone in the database
        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeCreate + 1);
        Telefone testTelefone = telefones.get(telefones.size() - 1);
        assertThat(testTelefone.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testTelefone.getDdd()).isEqualTo(DEFAULT_DDD);
        assertThat(testTelefone.getDdi()).isEqualTo(DEFAULT_DDI);
        assertThat(testTelefone.isFixo()).isEqualTo(DEFAULT_FIXO);

        // Validate the Telefone in ElasticSearch
        Telefone telefoneEs = telefoneSearchRepository.findOne(testTelefone.getId());
        assertThat(telefoneEs).isEqualToComparingFieldByField(testTelefone);
    }

    @Test
    @Transactional
    public void checkNumeroIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneRepository.findAll().size();
        // set the field null
        telefone.setNumero(null);

        // Create the Telefone, which fails.

        restTelefoneMockMvc.perform(post("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefone)))
                .andExpect(status().isBadRequest());

        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDddIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneRepository.findAll().size();
        // set the field null
        telefone.setDdd(null);

        // Create the Telefone, which fails.

        restTelefoneMockMvc.perform(post("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefone)))
                .andExpect(status().isBadRequest());

        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDdiIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneRepository.findAll().size();
        // set the field null
        telefone.setDdi(null);

        // Create the Telefone, which fails.

        restTelefoneMockMvc.perform(post("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefone)))
                .andExpect(status().isBadRequest());

        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFixoIsRequired() throws Exception {
        int databaseSizeBeforeTest = telefoneRepository.findAll().size();
        // set the field null
        telefone.setFixo(null);

        // Create the Telefone, which fails.

        restTelefoneMockMvc.perform(post("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(telefone)))
                .andExpect(status().isBadRequest());

        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllTelefones() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get all the telefones
        restTelefoneMockMvc.perform(get("/api/telefones?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(telefone.getId().intValue())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
                .andExpect(jsonPath("$.[*].ddd").value(hasItem(DEFAULT_DDD)))
                .andExpect(jsonPath("$.[*].ddi").value(hasItem(DEFAULT_DDI)))
                .andExpect(jsonPath("$.[*].fixo").value(hasItem(DEFAULT_FIXO.booleanValue())));
    }

    @Test
    @Transactional
    public void getTelefone() throws Exception {
        // Initialize the database
        telefoneRepository.saveAndFlush(telefone);

        // Get the telefone
        restTelefoneMockMvc.perform(get("/api/telefones/{id}", telefone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(telefone.getId().intValue()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO.toString()))
            .andExpect(jsonPath("$.ddd").value(DEFAULT_DDD))
            .andExpect(jsonPath("$.ddi").value(DEFAULT_DDI))
            .andExpect(jsonPath("$.fixo").value(DEFAULT_FIXO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingTelefone() throws Exception {
        // Get the telefone
        restTelefoneMockMvc.perform(get("/api/telefones/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTelefone() throws Exception {
        // Initialize the database
        telefoneService.save(telefone);

        int databaseSizeBeforeUpdate = telefoneRepository.findAll().size();

        // Update the telefone
        Telefone updatedTelefone = new Telefone();
        updatedTelefone.setId(telefone.getId());
        updatedTelefone.setNumero(UPDATED_NUMERO);
        updatedTelefone.setDdd(UPDATED_DDD);
        updatedTelefone.setDdi(UPDATED_DDI);
        updatedTelefone.setFixo(UPDATED_FIXO);

        restTelefoneMockMvc.perform(put("/api/telefones")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTelefone)))
                .andExpect(status().isOk());

        // Validate the Telefone in the database
        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeUpdate);
        Telefone testTelefone = telefones.get(telefones.size() - 1);
        assertThat(testTelefone.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testTelefone.getDdd()).isEqualTo(UPDATED_DDD);
        assertThat(testTelefone.getDdi()).isEqualTo(UPDATED_DDI);
        assertThat(testTelefone.isFixo()).isEqualTo(UPDATED_FIXO);

        // Validate the Telefone in ElasticSearch
        Telefone telefoneEs = telefoneSearchRepository.findOne(testTelefone.getId());
        assertThat(telefoneEs).isEqualToComparingFieldByField(testTelefone);
    }

    @Test
    @Transactional
    public void deleteTelefone() throws Exception {
        // Initialize the database
        telefoneService.save(telefone);

        int databaseSizeBeforeDelete = telefoneRepository.findAll().size();

        // Get the telefone
        restTelefoneMockMvc.perform(delete("/api/telefones/{id}", telefone.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean telefoneExistsInEs = telefoneSearchRepository.exists(telefone.getId());
        assertThat(telefoneExistsInEs).isFalse();

        // Validate the database is empty
        List<Telefone> telefones = telefoneRepository.findAll();
        assertThat(telefones).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchTelefone() throws Exception {
        // Initialize the database
        telefoneService.save(telefone);

        // Search the telefone
        restTelefoneMockMvc.perform(get("/api/_search/telefones?query=id:" + telefone.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(telefone.getId().intValue())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO.toString())))
            .andExpect(jsonPath("$.[*].ddd").value(hasItem(DEFAULT_DDD)))
            .andExpect(jsonPath("$.[*].ddi").value(hasItem(DEFAULT_DDI)))
            .andExpect(jsonPath("$.[*].fixo").value(hasItem(DEFAULT_FIXO.booleanValue())));
    }
}
