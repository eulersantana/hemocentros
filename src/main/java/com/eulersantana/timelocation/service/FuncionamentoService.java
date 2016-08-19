package com.eulersantana.timelocation.service;

import com.eulersantana.timelocation.domain.Funcionamento;
import com.eulersantana.timelocation.repository.FuncionamentoRepository;
import com.eulersantana.timelocation.repository.search.FuncionamentoSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Funcionamento.
 */
@Service
@Transactional
public class FuncionamentoService {

    private final Logger log = LoggerFactory.getLogger(FuncionamentoService.class);
    
    @Inject
    private FuncionamentoRepository funcionamentoRepository;
    
    @Inject
    private FuncionamentoSearchRepository funcionamentoSearchRepository;
    
    /**
     * Save a funcionamento.
     * 
     * @param funcionamento the entity to save
     * @return the persisted entity
     */
    public Funcionamento save(Funcionamento funcionamento) {
        log.debug("Request to save Funcionamento : {}", funcionamento);
        Funcionamento result = funcionamentoRepository.save(funcionamento);
        funcionamentoSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the funcionamentos.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Funcionamento> findAll() {
        log.debug("Request to get all Funcionamentos");
        List<Funcionamento> result = funcionamentoRepository.findAll();
        return result;
    }

    /**
     *  Get one funcionamento by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Funcionamento findOne(Long id) {
        log.debug("Request to get Funcionamento : {}", id);
        Funcionamento funcionamento = funcionamentoRepository.findOne(id);
        return funcionamento;
    }

    /**
     *  Delete the  funcionamento by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Funcionamento : {}", id);
        funcionamentoRepository.delete(id);
        funcionamentoSearchRepository.delete(id);
    }

    /**
     * Search for the funcionamento corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Funcionamento> search(String query) {
        log.debug("Request to search Funcionamentos for query {}", query);
        return StreamSupport
            .stream(funcionamentoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
