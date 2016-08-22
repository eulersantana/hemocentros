package com.eulersantana.timelocation.web.rest;

import com.eulersantana.timelocation.TimeLocationApp;
import com.eulersantana.timelocation.domain.Funcionamento;
import com.eulersantana.timelocation.repository.FuncionamentoRepository;
import com.eulersantana.timelocation.service.FuncionamentoService;
import com.eulersantana.timelocation.repository.search.FuncionamentoSearchRepository;

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
 * Test class for the FuncionamentoResource REST controller.
 *
 * @see FuncionamentoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TimeLocationApp.class)
@WebAppConfiguration
@IntegrationTest
public class FuncionamentoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_DIA = "AAAAA";
    private static final String UPDATED_DIA = "BBBBB";

    private static final ZonedDateTime DEFAULT_HORA_INICIO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HORA_INICIO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HORA_INICIO_STR = dateTimeFormatter.format(DEFAULT_HORA_INICIO);

    private static final ZonedDateTime DEFAULT_HORA_FIM = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_HORA_FIM = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_HORA_FIM_STR = dateTimeFormatter.format(DEFAULT_HORA_FIM);

    @Inject
    private FuncionamentoRepository funcionamentoRepository;

    @Inject
    private FuncionamentoService funcionamentoService;

    @Inject
    private FuncionamentoSearchRepository funcionamentoSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFuncionamentoMockMvc;

    private Funcionamento funcionamento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FuncionamentoResource funcionamentoResource = new FuncionamentoResource();
        ReflectionTestUtils.setField(funcionamentoResource, "funcionamentoService", funcionamentoService);
        this.restFuncionamentoMockMvc = MockMvcBuilders.standaloneSetup(funcionamentoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        funcionamentoSearchRepository.deleteAll();
        funcionamento = new Funcionamento();
        funcionamento.setDia(DEFAULT_DIA);
        funcionamento.setHora_inicio(DEFAULT_HORA_INICIO);
        funcionamento.setHora_fim(DEFAULT_HORA_FIM);
    }

    @Test
    @Transactional
    public void createFuncionamento() throws Exception {
        int databaseSizeBeforeCreate = funcionamentoRepository.findAll().size();

        // Create the Funcionamento

        restFuncionamentoMockMvc.perform(post("/api/funcionamentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionamento)))
                .andExpect(status().isCreated());

        // Validate the Funcionamento in the database
        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeCreate + 1);
        Funcionamento testFuncionamento = funcionamentos.get(funcionamentos.size() - 1);
        assertThat(testFuncionamento.getDia()).isEqualTo(DEFAULT_DIA);
        assertThat(testFuncionamento.getHora_inicio()).isEqualTo(DEFAULT_HORA_INICIO);
        assertThat(testFuncionamento.getHora_fim()).isEqualTo(DEFAULT_HORA_FIM);

        // Validate the Funcionamento in ElasticSearch
        Funcionamento funcionamentoEs = funcionamentoSearchRepository.findOne(testFuncionamento.getId());
        assertThat(funcionamentoEs).isEqualToComparingFieldByField(testFuncionamento);
    }

    @Test
    @Transactional
    public void checkDiaIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionamentoRepository.findAll().size();
        // set the field null
        funcionamento.setDia(null);

        // Create the Funcionamento, which fails.

        restFuncionamentoMockMvc.perform(post("/api/funcionamentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionamento)))
                .andExpect(status().isBadRequest());

        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHora_inicioIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionamentoRepository.findAll().size();
        // set the field null
        funcionamento.setHora_inicio(null);

        // Create the Funcionamento, which fails.

        restFuncionamentoMockMvc.perform(post("/api/funcionamentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionamento)))
                .andExpect(status().isBadRequest());

        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkHora_fimIsRequired() throws Exception {
        int databaseSizeBeforeTest = funcionamentoRepository.findAll().size();
        // set the field null
        funcionamento.setHora_fim(null);

        // Create the Funcionamento, which fails.

        restFuncionamentoMockMvc.perform(post("/api/funcionamentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(funcionamento)))
                .andExpect(status().isBadRequest());

        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllFuncionamentos() throws Exception {
        // Initialize the database
        funcionamentoRepository.saveAndFlush(funcionamento);

        // Get all the funcionamentos
        restFuncionamentoMockMvc.perform(get("/api/funcionamentos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(funcionamento.getId().intValue())))
                .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.toString())))
                .andExpect(jsonPath("$.[*].hora_inicio").value(hasItem(DEFAULT_HORA_INICIO_STR)))
                .andExpect(jsonPath("$.[*].hora_fim").value(hasItem(DEFAULT_HORA_FIM_STR)));
    }

    @Test
    @Transactional
    public void getFuncionamento() throws Exception {
        // Initialize the database
        funcionamentoRepository.saveAndFlush(funcionamento);

        // Get the funcionamento
        restFuncionamentoMockMvc.perform(get("/api/funcionamentos/{id}", funcionamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(funcionamento.getId().intValue()))
            .andExpect(jsonPath("$.dia").value(DEFAULT_DIA.toString()))
            .andExpect(jsonPath("$.hora_inicio").value(DEFAULT_HORA_INICIO_STR))
            .andExpect(jsonPath("$.hora_fim").value(DEFAULT_HORA_FIM_STR));
    }

    @Test
    @Transactional
    public void getNonExistingFuncionamento() throws Exception {
        // Get the funcionamento
        restFuncionamentoMockMvc.perform(get("/api/funcionamentos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFuncionamento() throws Exception {
        // Initialize the database
        funcionamentoService.save(funcionamento);

        int databaseSizeBeforeUpdate = funcionamentoRepository.findAll().size();

        // Update the funcionamento
        Funcionamento updatedFuncionamento = new Funcionamento();
        updatedFuncionamento.setId(funcionamento.getId());
        updatedFuncionamento.setDia(UPDATED_DIA);
        updatedFuncionamento.setHora_inicio(UPDATED_HORA_INICIO);
        updatedFuncionamento.setHora_fim(UPDATED_HORA_FIM);

        restFuncionamentoMockMvc.perform(put("/api/funcionamentos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFuncionamento)))
                .andExpect(status().isOk());

        // Validate the Funcionamento in the database
        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeUpdate);
        Funcionamento testFuncionamento = funcionamentos.get(funcionamentos.size() - 1);
        assertThat(testFuncionamento.getDia()).isEqualTo(UPDATED_DIA);
        assertThat(testFuncionamento.getHora_inicio()).isEqualTo(UPDATED_HORA_INICIO);
        assertThat(testFuncionamento.getHora_fim()).isEqualTo(UPDATED_HORA_FIM);

        // Validate the Funcionamento in ElasticSearch
        Funcionamento funcionamentoEs = funcionamentoSearchRepository.findOne(testFuncionamento.getId());
        assertThat(funcionamentoEs).isEqualToComparingFieldByField(testFuncionamento);
    }

    @Test
    @Transactional
    public void deleteFuncionamento() throws Exception {
        // Initialize the database
        funcionamentoService.save(funcionamento);

        int databaseSizeBeforeDelete = funcionamentoRepository.findAll().size();

        // Get the funcionamento
        restFuncionamentoMockMvc.perform(delete("/api/funcionamentos/{id}", funcionamento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean funcionamentoExistsInEs = funcionamentoSearchRepository.exists(funcionamento.getId());
        assertThat(funcionamentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Funcionamento> funcionamentos = funcionamentoRepository.findAll();
        assertThat(funcionamentos).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchFuncionamento() throws Exception {
        // Initialize the database
        funcionamentoService.save(funcionamento);

        // Search the funcionamento
        restFuncionamentoMockMvc.perform(get("/api/_search/funcionamentos?query=id:" + funcionamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].id").value(hasItem(funcionamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].dia").value(hasItem(DEFAULT_DIA.toString())))
            .andExpect(jsonPath("$.[*].hora_inicio").value(hasItem(DEFAULT_HORA_INICIO_STR)))
            .andExpect(jsonPath("$.[*].hora_fim").value(hasItem(DEFAULT_HORA_FIM_STR)));
    }
}
