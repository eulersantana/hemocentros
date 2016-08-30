package com.eulersantana.timelocation.web.rest;

import com.eulersantana.timelocation.TimeLocationApp;
import com.eulersantana.timelocation.domain.Endereco;
import com.eulersantana.timelocation.repository.EnderecoRepository;
import com.eulersantana.timelocation.service.EnderecoService;
import com.eulersantana.timelocation.repository.search.EnderecoSearchRepository;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EnderecoResource REST controller.
 *
 * @see EnderecoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeLocationApp.class)
@WebAppConfiguration
@IntegrationTest
public class EnderecoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_RUA = "AAAAA";
    private static final String UPDATED_RUA = "BBBBB";

    private static final Integer DEFAULT_NUMERO = 1;
    private static final Integer UPDATED_NUMERO = 2;
    private static final String DEFAULT_CEP = "AAAAA";
    private static final String UPDATED_CEP = "BBBBB";
    private static final String DEFAULT_COMPLEMENTO = "AAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBB";

    private static final Double DEFAULT_LONGITUDE = 1D;
    private static final Double UPDATED_LONGITUDE = 2D;

    private static final Double DEFAULT_LATITUDE = 1D;
    private static final Double UPDATED_LATITUDE = 2D;

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_CREATED_AT_STR = dateTimeFormatter.format(DEFAULT_CREATED_AT);

    @Inject
    private EnderecoRepository enderecoRepository;

    @Inject
    private EnderecoService enderecoService;

    @Inject
    private EnderecoSearchRepository enderecoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnderecoResource enderecoResource = new EnderecoResource();
        ReflectionTestUtils.setField(enderecoResource, "enderecoService", enderecoService);
        this.restEnderecoMockMvc = MockMvcBuilders.standaloneSetup(enderecoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        enderecoSearchRepository.deleteAll();
        endereco = new Endereco();
        endereco.setRua(DEFAULT_RUA);
        endereco.setNumero(DEFAULT_NUMERO);
        endereco.setCep(DEFAULT_CEP);
        endereco.setComplemento(DEFAULT_COMPLEMENTO);
        endereco.setLongitude(DEFAULT_LONGITUDE);
        endereco.setLatitude(DEFAULT_LATITUDE);
        endereco.setCreatedAt(DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    public void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecos.get(enderecos.size() - 1);
        assertThat(testEndereco.getRua()).isEqualTo(DEFAULT_RUA);
        assertThat(testEndereco.getNumero()).isEqualTo(DEFAULT_NUMERO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);
        assertThat(testEndereco.getComplemento()).isEqualTo(DEFAULT_COMPLEMENTO);
        assertThat(testEndereco.getLongitude()).isEqualTo(DEFAULT_LONGITUDE);
        assertThat(testEndereco.getLatitude()).isEqualTo(DEFAULT_LATITUDE);
        assertThat(testEndereco.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);

        // Validate the Endereco in ElasticSearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void checkRuaIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setRua(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCepIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = enderecoRepository.findAll().size();
        // set the field null
        endereco.setCreatedAt(null);

        // Create the Endereco, which fails.

        restEnderecoMockMvc.perform(post("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(endereco)))
                .andExpect(status().isBadRequest());

        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecos
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
                .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA.toString())))
                .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
                .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
                .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
                .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
                .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)));
    }

    @Test
    @Transactional
    public void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.rua").value(DEFAULT_RUA.toString()))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO.toString()))
            .andExpect(jsonPath("$.longitude").value(DEFAULT_LONGITUDE.doubleValue()))
            .andExpect(jsonPath("$.latitude").value(DEFAULT_LATITUDE.doubleValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT_STR));
    }

    @Test
    @Transactional
    public void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndereco() throws Exception {
        // Initialize the database
        enderecoService.save(endereco);

        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        Endereco updatedEndereco = new Endereco();
        updatedEndereco.setId(endereco.getId());
        updatedEndereco.setRua(UPDATED_RUA);
        updatedEndereco.setNumero(UPDATED_NUMERO);
        updatedEndereco.setCep(UPDATED_CEP);
        updatedEndereco.setComplemento(UPDATED_COMPLEMENTO);
        updatedEndereco.setLongitude(UPDATED_LONGITUDE);
        updatedEndereco.setLatitude(UPDATED_LATITUDE);
        updatedEndereco.setCreatedAt(UPDATED_CREATED_AT);

        restEnderecoMockMvc.perform(put("/api/enderecos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEndereco)))
                .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecos.get(enderecos.size() - 1);
        assertThat(testEndereco.getRua()).isEqualTo(UPDATED_RUA);
        assertThat(testEndereco.getNumero()).isEqualTo(UPDATED_NUMERO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);
        assertThat(testEndereco.getComplemento()).isEqualTo(UPDATED_COMPLEMENTO);
        assertThat(testEndereco.getLongitude()).isEqualTo(UPDATED_LONGITUDE);
        assertThat(testEndereco.getLatitude()).isEqualTo(UPDATED_LATITUDE);
        assertThat(testEndereco.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);

        // Validate the Endereco in ElasticSearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoService.save(endereco);

        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Get the endereco
        restEnderecoMockMvc.perform(delete("/api/enderecos/{id}", endereco.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean enderecoExistsInEs = enderecoSearchRepository.exists(endereco.getId());
        assertThat(enderecoExistsInEs).isFalse();

        // Validate the database is empty
        List<Endereco> enderecos = enderecoRepository.findAll();
        assertThat(enderecos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEndereco() throws Exception {
        // Initialize the database
        enderecoService.save(endereco);

        // Search the endereco
        restEnderecoMockMvc.perform(get("/api/_search/enderecos?query=id:" + endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].rua").value(hasItem(DEFAULT_RUA.toString())))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO.toString())))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(DEFAULT_LONGITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(DEFAULT_LATITUDE.doubleValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT_STR)));
    }
}
